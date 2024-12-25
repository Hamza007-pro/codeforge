"use client";

import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Label } from "@/components/ui/label";
import { useProjectStore } from "@/lib/store";
import { cn } from "@/lib/utils";

export default function ArchitecturePattern() {
  const { setArchitecturePattern } = useProjectStore();

  return (
    <div className="space-y-3">
      <h2 className="text-lg font-semibold">Architecture Pattern</h2>
      <RadioGroup
        defaultValue="mvvm"
        className="grid grid-cols-3 gap-3"
        onValueChange={(value) => setArchitecturePattern(value.toUpperCase())}
      >
        <Label
          htmlFor="mvvm"
          className={cn(
            "flex cursor-pointer flex-col items-center justify-between rounded-md border border-muted p-3",
            "hover:bg-accent hover:text-accent-foreground transition-all",
            "[&:has([data-state=checked])]:border-primary [&:has([data-state=checked])]:bg-gradient-to-r [&:has([data-state=checked])]:from-[hsl(var(--primary-start))] [&:has([data-state=checked])]:to-[hsl(var(--primary-end))] [&:has([data-state=checked])]:text-white"
          )}
        >
          <RadioGroupItem value="mvvm" id="mvvm" className="sr-only" />
          <span className="font-semibold mb-1">MVVM</span>
          <span className="font-light text-xs text-center [&:has([data-state=checked])]:text-white/90">
            Model View View Model
          </span>
        </Label>
        <Label
          htmlFor="mvc"
          className={cn(
            "flex cursor-pointer flex-col items-center justify-between rounded-md border border-muted p-3",
            "hover:bg-accent hover:text-accent-foreground transition-all",
            "[&:has([data-state=checked])]:border-primary [&:has([data-state=checked])]:bg-gradient-to-r [&:has([data-state=checked])]:from-[hsl(var(--primary-start))] [&:has([data-state=checked])]:to-[hsl(var(--primary-end))] [&:has([data-state=checked])]:text-white"
          )}
        >
          <RadioGroupItem value="mvc" id="mvc" className="sr-only" />
          <span className="font-semibold mb-1">MVC</span>
          <span className="font-light text-xs text-center [&:has([data-state=checked])]:text-white/90">
            Model View Controller
          </span>
        </Label>
        <Label
          htmlFor="mvp"
          className={cn(
            "flex cursor-pointer flex-col items-center justify-between rounded-md border border-muted p-3",
            "hover:bg-accent hover:text-accent-foreground transition-all",
            "[&:has([data-state=checked])]:border-primary [&:has([data-state=checked])]:bg-gradient-to-r [&:has([data-state=checked])]:from-[hsl(var(--primary-start))] [&:has([data-state=checked])]:to-[hsl(var(--primary-end))] [&:has([data-state=checked])]:text-white"
          )}
        >
          <RadioGroupItem value="mvp" id="mvp" className="sr-only" />
          <span className="font-semibold mb-1">MVP</span>
          <span className="font-light text-xs text-center [&:has([data-state=checked])]:text-white/90">
            Model View Presenter
          </span>
        </Label>
      </RadioGroup>
    </div>
  );
}
