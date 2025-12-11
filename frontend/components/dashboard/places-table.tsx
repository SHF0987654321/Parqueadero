"use client"

import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Car, Bike, Bus } from "lucide-react"
import type { Lugar } from "@/lib/api"

interface PlacesTableProps {
  places: Lugar[]
  isLoading: boolean
  title: string
  emptyMessage: string
}

const typeIcons = {
  CARRO: Car,
  MOTO: Bike,
  BUS: Bus,
}

const typeLabels = {
  CARRO: "Carro",
  MOTO: "Moto",
  BUS: "Bus",
}

export function PlacesTable({ places, isLoading, title, emptyMessage }: PlacesTableProps) {
  if (isLoading) {
    return (
      <Card className="border-border">
        <CardHeader>
          <CardTitle className="text-card-foreground">{title}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="animate-pulse space-y-4">
            {[...Array(5)].map((_, i) => (
              <div key={i} className="h-12 bg-muted rounded" />
            ))}
          </div>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card className="border-border">
      <CardHeader>
        <CardTitle className="text-card-foreground">{title}</CardTitle>
      </CardHeader>
      <CardContent>
        {places.length === 0 ? (
          <div className="text-center py-8 text-muted-foreground">{emptyMessage}</div>
        ) : (
          <Table>
            <TableHeader>
              <TableRow className="border-border">
                <TableHead className="text-muted-foreground">Nombre</TableHead>
                <TableHead className="text-muted-foreground">Tipo</TableHead>
                <TableHead className="text-muted-foreground">Estado</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {places.map((place) => {
                const Icon = typeIcons[place.tipo]
                return (
                  <TableRow key={place.id} className="border-border">
                    <TableCell className="font-medium text-card-foreground">{place.nombre}</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-2 text-muted-foreground">
                        <Icon className="h-4 w-4" />
                        {typeLabels[place.tipo]}
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge
                        variant={place.estado === "LIBRE" ? "default" : "secondary"}
                        className={
                          place.estado === "LIBRE"
                            ? "bg-success text-success-foreground"
                            : "bg-destructive text-destructive-foreground"
                        }
                      >
                        {place.estado === "LIBRE" ? "Libre" : "Ocupado"}
                      </Badge>
                    </TableCell>
                  </TableRow>
                )
              })}
            </TableBody>
          </Table>
        )}
      </CardContent>
    </Card>
  )
}
