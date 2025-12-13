"use client"

import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group"
import { Car, Warehouse, ListChecks } from "lucide-react"

// Definimos el tipo para asegurar consistencia con AdminPage
type MovementFilterType = "ACTIVE" | "HISTORY" | "ALL"

interface FilterButtonsProps {
  currentFilter: MovementFilterType
  onSelect: (filter: MovementFilterType) => void
}

export function FilterButtons({ currentFilter, onSelect }: FilterButtonsProps) {
  return (
    <ToggleGroup
      type="single"
      value={currentFilter}
      onValueChange={(value: MovementFilterType) => {
        if (value) onSelect(value)
      }}
      className="gap-1 bg-background rounded-lg p-1 border border-border"
    >
      <ToggleGroupItem 
        value="ACTIVE" 
        className="h-8 text-xs px-2 data-[state=on]:bg-success data-[state=on]:text-white"
      >
        <Warehouse className="h-3.5 w-3.5 mr-1.5" />
        Parqueados
      </ToggleGroupItem>
      
      <ToggleGroupItem 
        value="HISTORY" 
        className="h-8 text-xs px-2 data-[state=on]:bg-blue-600 data-[state=on]:text-white"
      >
        <ListChecks className="h-3.5 w-3.5 mr-1.5" />
        Historial
      </ToggleGroupItem>
      
      <ToggleGroupItem 
        value="ALL" 
        className="h-8 text-xs px-2"
      >
        <Car className="h-3.5 w-3.5 mr-1.5" />
        Todos
      </ToggleGroupItem>
    </ToggleGroup>
  )
}