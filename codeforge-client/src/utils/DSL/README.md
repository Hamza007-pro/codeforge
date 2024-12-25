# SimplSchema DSL Documentation

SimplSchema is a human-friendly domain-specific language (DSL) for defining database structures without writing SQL. It provides an intuitive way to define data models, their fields, and relationships between them.

## Table of Contents
- [Basic Syntax](#basic-syntax)
  - [Models](#models)
  - [Fields](#fields)
  - [Relationships](#relationships)
- [Data Types](#data-types)
- [Field Constraints](#field-constraints)
- [Relationship Types](#relationship-types)
- [Examples](#examples)

## Basic Syntax

### Models

Models represent database tables or entities. They are defined using the `model` keyword followed by the model name and fields enclosed in curly braces.

```
model User {
  id: number required
  name: text required
  email: text unique
}
```

### Fields

Fields are defined within models using the format: `fieldName: fieldType [constraints]`

```
model Product {
  id: number required
  name: text required
  price: number required positive
  description: text long
  inStock: boolean default:true
}
```

### Relationships

Relationships between models are defined using the `link` keyword:

```
// One-way relationship
link User -> Order (one to many)

// Two-way relationship with attributes
link Order <-> Product (many to many) {
  quantity: number positive required
  price_at_order: number positive
}
```

## Data Types

SimplSchema supports the following data types:

| Type      | Description                           | Example Usage                    |
|-----------|---------------------------------------|----------------------------------|
| `text`    | Text/string data                      | `name: text`                     |
| `number`  | Numeric values (integers or decimals)  | `price: number`                  |
| `date`    | Date and time values                  | `created_at: date`               |
| `boolean` | True/false values                     | `isActive: boolean`              |
| `list`    | Array/list of values                  | `tags: list`                     |
| `file`    | File attachments or references        | `avatar: file`                   |

## Field Constraints

You can apply various constraints to fields:

| Constraint  | Description                                | Example                        |
|------------|--------------------------------------------|---------------------------------|
| `required` | Field must have a value                    | `name: text required`          |
| `unique`   | Value must be unique across all records    | `email: text unique`           |
| `positive` | Number must be greater than zero           | `price: number positive`       |
| `long`     | Indicates a long text field                | `content: text long`           |
| `default:` | Sets a default value                       | `active: boolean default:true` |

Multiple constraints can be combined:
```
email: text required unique
price: number required positive
```

## Relationship Types

Three types of relationships are supported:

1. **One-to-One**:
   ```
   link User -> Profile (one to one)
   ```

2. **One-to-Many**:
   ```
   link User -> Order (one to many)
   ```

3. **Many-to-Many**:
   ```
   link Student <-> Course (many to many)
   ```

Relationship attributes can be added using curly braces:
```
link Order <-> Product (many to many) {
  quantity: number positive required
  price_at_order: number positive
}
```

## Examples

### Complete E-commerce Schema Example

```
model User {
  id: number required
  username: text required unique
  email: text required unique
  password: text required
  created_at: date
}

model Product {
  id: number required
  name: text required
  description: text long
  price: number required positive
  stock: number required positive
  created_at: date
}

model Order {
  id: number required
  status: text required
  total: number required positive
  created_at: date
}

model Address {
  id: number required
  street: text required
  city: text required
  country: text required
  postal_code: text required
}

// Relationships
link User -> Order (one to many)
link User -> Address (one to many)
link Order <-> Product (many to many) {
  quantity: number positive required
  price_at_order: number positive required
}
```

### Blog Schema Example

```
model Author {
  id: number required
  name: text required
  bio: text long
  email: text required unique
}

model Post {
  id: number required
  title: text required
  content: text long required
  published: boolean default:false
  created_at: date
}

model Category {
  id: number required
  name: text required unique
  description: text
}

model Comment {
  id: number required
  content: text required
  created_at: date
}

// Relationships
link Author -> Post (one to many)
link Post -> Comment (one to many)
link Post <-> Category (many to many)
```

## Best Practices

1. Use descriptive model and field names
2. Always include an ID field for each model
3. Add timestamps (created_at, updated_at) for tracking
4. Use appropriate data types and constraints
5. Document relationships clearly with meaningful names
6. Keep models focused and avoid too many fields
7. Use consistent naming conventions

## Notes

- Comments start with `//`
- Field names should be in camelCase or snake_case
- Model names should start with a capital letter
- Relationship definitions should come after model definitions
- Indentation is recommended for readability but not required
