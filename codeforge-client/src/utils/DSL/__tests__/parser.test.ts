import { parseSchema } from "..";

describe("SimplSchema Parser", () => {
    it("should parse a simple model definition", () => {
        const input = `
            model User {
                username: text required unique
                email: text required unique
                age: number positive
                bio: text long
                active: boolean default:true
            }
        `;

        const schema = parseSchema(input);
        expect(schema.models).toHaveLength(1);
        
        const userModel = schema.models[0];
        expect(userModel.name).toBe("User");
        expect(userModel.fields).toHaveLength(5);

        // Check username field
        const usernameField = userModel.fields[0];
        expect(usernameField.name).toBe("username");
        expect(usernameField.type).toEqual({ kind: "text" });
        expect(usernameField.constraints).toContainEqual({ kind: "required" });
        expect(usernameField.constraints).toContainEqual({ kind: "unique" });

        // Check age field
        const ageField = userModel.fields[2];
        expect(ageField.name).toBe("age");
        expect(ageField.type).toEqual({ kind: "number" });
        expect(ageField.constraints).toContainEqual({ kind: "positive" });

        // Check bio field
        const bioField = userModel.fields[3];
        expect(bioField.name).toBe("bio");
        expect(bioField.type).toEqual({ kind: "text" });
        expect(bioField.constraints).toContainEqual({ kind: "long" });

        // Check active field
        const activeField = userModel.fields[4];
        expect(activeField.name).toBe("active");
        expect(activeField.type).toEqual({ kind: "boolean" });
        expect(activeField.constraints).toContainEqual({ kind: "default", value: true });
    });

    it("should parse relationships between models", () => {
        const input = `
            model User {
                username: text required
                email: text required unique
            }

            model Post {
                title: text required
                content: text long
            }

            link User -> Post (one to many)
            link Post <-> Tag (many to many) {
                added_at: date required
            }
        `;

        const schema = parseSchema(input);
        expect(schema.models).toHaveLength(2);
        expect(schema.links).toHaveLength(2);

        // Check User -> Post link
        const userPostLink = schema.links[0];
        expect(userPostLink.from).toBe("User");
        expect(userPostLink.to).toBe("Post");
        expect(userPostLink.type).toBe("one-to-many");
        expect(userPostLink.bidirectional).toBe(false);

        // Check Post <-> Tag link
        const postTagLink = schema.links[1];
        expect(postTagLink.from).toBe("Post");
        expect(postTagLink.to).toBe("Tag");
        expect(postTagLink.type).toBe("many-to-many");
        expect(postTagLink.bidirectional).toBe(true);
        expect(postTagLink.attributes).toBeDefined();
        expect(postTagLink.attributes![0]).toEqual({
            name: "added_at",
            type: { kind: "date" },
            constraints: [{ kind: "required" }]
        });
    });

    it("should parse all field types and constraints", () => {
        const input = `
            model Product {
                name: text required
                price: number positive required
                description: text long
                created_at: date
                is_active: boolean default:true
                tags: list
                image: file
            }
        `;

        const schema = parseSchema(input);
        const productModel = schema.models[0];

        // Check all field types
        expect(productModel.fields[0].type).toEqual({ kind: "text" });
        expect(productModel.fields[1].type).toEqual({ kind: "number" });
        expect(productModel.fields[2].type).toEqual({ kind: "text" });
        expect(productModel.fields[3].type).toEqual({ kind: "date" });
        expect(productModel.fields[4].type).toEqual({ kind: "boolean" });
        expect(productModel.fields[5].type).toEqual({ kind: "list" });
        expect(productModel.fields[6].type).toEqual({ kind: "file" });

        // Check constraints
        expect(productModel.fields[0].constraints).toContainEqual({ kind: "required" });
        expect(productModel.fields[1].constraints).toContainEqual({ kind: "positive" });
        expect(productModel.fields[1].constraints).toContainEqual({ kind: "required" });
        expect(productModel.fields[2].constraints).toContainEqual({ kind: "long" });
        expect(productModel.fields[4].constraints).toContainEqual({ kind: "default", value: true });
    });
});
