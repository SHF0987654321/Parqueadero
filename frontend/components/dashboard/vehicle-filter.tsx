"use client"

import { Button } from "@/components/ui/button"
import { Car, Bike, Bus, LayoutGrid } from "lucide-react"

interface VehicleFilterProps {
  selected: string
  onSelect: (type: string) => void
}

export function VehicleFilter({ selected, onSelect }: VehicleFilterProps) {
  const filters = [
    { value: "", label: "Todos", icon: LayoutGrid },
    { value: "CARRO", label: "Carros", icon: Car },
    { value: "MOTO", label: "Motos", icon: Bike },
    { value: "BUS", label: "Buses", icon: Bus },
  ]

  return (
    <div className="flex flex-wrap gap-2">
      {filters.map((filter) => (
        <Button
          key={filter.value}
          variant={selected === filter.value ? "default" : "outline"}
          size="sm"
          onClick={() => onSelect(filter.value)}
          className={selected === filter.value ? "bg-primary text-primary-foreground" : "border-border"}
        >
          <filter.icon className="mr-2 h-4 w-4" />
          {filter.label}
        </Button>
      ))}
    </div>
  )
}
