import { Constraint } from "@/models/core";
import { Field } from "@/models/core/field";

function hasConstraintOfType(
    field: Field,
    constraintType: string
  ): boolean {
    if (field.getConstraints() && field.getConstraints() instanceof Map) {
      for (const [, constraint] of field.getConstraints()) {
        if (constraint.type === constraintType) {
          return true;
        }
      }
    }
    return false;
}

function findConstraintOfType(
    field: Field,
    constraintType: string
  ): Constraint | null {
    if (field.getConstraints() && field.getConstraints() instanceof Map) {
      for (const [, constraint] of field.getConstraints()) {
        if (constraint.type === constraintType) {
          return constraint;
        }
      }
    }
    return null;
  }


export { hasConstraintOfType, findConstraintOfType }