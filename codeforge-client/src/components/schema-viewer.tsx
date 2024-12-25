"use client";

import React from "react";
import { Schema } from "@/utils/DSL/visitor";
import { SimplSchemaService } from "@/utils/DSL/service";

interface SchemaViewerProps {
  schema: Schema | null;
}

export function SchemaViewer({ schema }: SchemaViewerProps) {
  const canvasRef = React.useRef<HTMLCanvasElement>(null);
  const [modelPositions, setModelPositions] = React.useState<Map<string, { x: number; y: number }>>(new Map());
  const [dragState, setDragState] = React.useState<{
    isDragging: boolean;
    modelName: string | null;
    startX: number;
    startY: number;
    offsetX: number;
    offsetY: number;
  }>({
    isDragging: false,
    modelName: null,
    startX: 0,
    startY: 0,
    offsetX: 0,
    offsetY: 0,
  });

  const drawCanvas = React.useCallback(() => {
    if (!schema || !canvasRef.current) return;

    const canvas = canvasRef.current;
    const ctx = canvas.getContext("2d");
    if (!ctx) return;

    // Set canvas size
    const container = canvas.parentElement;
    if (!container) return;
    canvas.width = container.clientWidth;
    canvas.height = container.clientHeight;

    // Clear canvas with background color
    ctx.fillStyle = "#f8fafc";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    // Get models and relationships
    const models = SimplSchemaService.getModels(schema);
    const relationships = SimplSchemaService.getRelationships(schema);

    // Initialize positions if not set
    if (modelPositions.size === 0) {
      const newPositions = new Map<string, { x: number; y: number }>();
      const modelWidth = 200;
      const modelHeight = 150;
      const padding = 20;
      const startX = padding;
      const startY = padding;
      const modelsPerRow = Math.floor((canvas.width - padding) / (modelWidth + padding));

      models.forEach((model, index) => {
        const row = Math.floor(index / modelsPerRow);
        const col = index % modelsPerRow;
        const x = startX + col * (modelWidth + padding);
        const y = startY + row * (modelHeight + padding);
        newPositions.set(model.name, { x, y });
      });
      setModelPositions(newPositions);
      return;
    }

    // Draw models
    models.forEach((model) => {
      const pos = modelPositions.get(model.name);
      if (!pos) return;
      const { x, y } = pos;
      const modelWidth = 200;
      const modelHeight = 150;

      // Draw model box
      ctx.fillStyle = "#ffffff";
      ctx.strokeStyle = "#e2e8f0";
      ctx.lineWidth = 1;
      ctx.beginPath();
      ctx.roundRect(x, y, modelWidth, modelHeight, 8);
      ctx.fill();
      ctx.stroke();

      // Draw model name
      ctx.fillStyle = "#1e293b";
      ctx.font = "bold 14px system-ui";
      ctx.textAlign = "left";
      ctx.fillText(model.name, x + 12, y + 24);

      // Draw fields
      ctx.font = "12px system-ui";
      model.fields.forEach((field, fieldIndex) => {
        const fieldY = y + 48 + fieldIndex * 20;
        if (fieldY < y + modelHeight - 12) {
          ctx.fillStyle = "#64748b";
          ctx.fillText(
            `${field.name}: ${field.type.kind}${field.constraints.map(c => ` ${c.kind}`).join("")}`,
            x + 12,
            fieldY
          );
        }
      });
    });

    // Draw relationships
    ctx.strokeStyle = "#0f172a";
    ctx.lineWidth = 1;
    relationships.forEach((rel) => {
      const fromPos = modelPositions.get(rel.from);
      const toPos = modelPositions.get(rel.to);
      if (!fromPos || !toPos) return;

      const modelWidth = 200;
      const modelHeight = 150;
      const fromX = fromPos.x + modelWidth;
      const fromY = fromPos.y + modelHeight / 2;
      const toX = toPos.x;
      const toY = toPos.y + modelHeight / 2;

      // Draw curved line
      ctx.beginPath();
      ctx.moveTo(fromX, fromY);
      const controlPoint1X = fromX + (toX - fromX) / 3;
      const controlPoint1Y = fromY;
      const controlPoint2X = fromX + 2 * (toX - fromX) / 3;
      const controlPoint2Y = toY;
      ctx.bezierCurveTo(controlPoint1X, controlPoint1Y, controlPoint2X, controlPoint2Y, toX, toY);
      ctx.stroke();

      // Draw arrow
      const arrowSize = 8;
      const angle = Math.atan2(toY - controlPoint2Y, toX - controlPoint2X);
      ctx.beginPath();
      ctx.moveTo(toX, toY);
      ctx.lineTo(
        toX - arrowSize * Math.cos(angle - Math.PI / 6),
        toY - arrowSize * Math.sin(angle - Math.PI / 6)
      );
      ctx.moveTo(toX, toY);
      ctx.lineTo(
        toX - arrowSize * Math.cos(angle + Math.PI / 6),
        toY - arrowSize * Math.sin(angle + Math.PI / 6)
      );
      ctx.stroke();

      // Draw relationship type
      ctx.fillStyle = "#64748b";
      ctx.font = "12px system-ui";
      ctx.textAlign = "center";
      const textX = fromX + (toX - fromX) / 2;
      const textY = fromY + (toY - fromY) / 2 - 10;
      ctx.fillText(rel.type, textX, textY);
    });
  }, [schema, modelPositions]);

  const handleMouseDown = (e: React.MouseEvent<HTMLCanvasElement>) => {
    if (!canvasRef.current || !schema) return;

    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;

    // Check if click is inside any model
    const models = SimplSchemaService.getModels(schema);
    const modelWidth = 200;
    const modelHeight = 150;

    for (const model of models) {
      const pos = modelPositions.get(model.name);
      if (!pos) continue;

      if (
        x >= pos.x &&
        x <= pos.x + modelWidth &&
        y >= pos.y &&
        y <= pos.y + modelHeight
      ) {
        setDragState({
          isDragging: true,
          modelName: model.name,
          startX: x,
          startY: y,
          offsetX: x - pos.x,
          offsetY: y - pos.y,
        });
        break;
      }
    }
  };

  const handleMouseMove = (e: React.MouseEvent<HTMLCanvasElement>) => {
    if (!dragState.isDragging || !dragState.modelName || !canvasRef.current) return;

    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;

    const newPositions = new Map(modelPositions);
    newPositions.set(dragState.modelName, {
      x: x - dragState.offsetX,
      y: y - dragState.offsetY,
    });
    setModelPositions(newPositions);
  };

  const handleMouseUp = () => {
    setDragState({
      isDragging: false,
      modelName: null,
      startX: 0,
      startY: 0,
      offsetX: 0,
      offsetY: 0,
    });
  };

  React.useEffect(() => {
    drawCanvas();
  }, [drawCanvas]);

  return (
    <div className="w-full h-full bg-background">
      <canvas
        ref={canvasRef}
        className="w-full h-full"
        onMouseDown={handleMouseDown}
        onMouseMove={handleMouseMove}
        onMouseUp={handleMouseUp}
        onMouseLeave={handleMouseUp}
      />
    </div>
  );
}
