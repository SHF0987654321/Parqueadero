"use client"

import { Movimiento } from "@/lib/api"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { Card, CardContent } from "@/components/ui/card"
import { format } from "date-fns"
import { es } from "date-fns/locale"

interface MovementsTableProps {
  data: Movimiento[]
  isLoading: boolean
}

export function MovementsTable({ data, isLoading }: MovementsTableProps) {
  if (isLoading) {
    return (
      <div className="space-y-3">
        {[...Array(5)].map((_, i) => (
          <div key={i} className="h-12 w-full bg-muted animate-pulse rounded-md" />
        ))}
      </div>
    )
  }

  return (
    <Card className="border-border">
      <CardContent className="p-0">
        <div className="max-h-[500px] overflow-y-auto">
          <Table>
            <TableHeader className="bg-muted/50 sticky top-0 z-10">
              <TableRow>
                <TableHead>Placa</TableHead>
                <TableHead>Tipo</TableHead>
                <TableHead>Lugar</TableHead>
                <TableHead>Estado</TableHead>
                <TableHead>Entrada</TableHead>
                <TableHead>Salida</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {data.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} className="h-32 text-center text-muted-foreground">
                    No se encontraron movimientos registrados.
                  </TableCell>
                </TableRow>
              ) : (
                data.map((mov) => {
                  const isActive = !mov.fechaSalida
                  
                  return (
                    <TableRow key={mov.id} className={isActive ? "bg-success/5 hover:bg-success/10" : ""}>
                      <TableCell className="font-bold">{mov.placa}</TableCell>
                      <TableCell className="text-xs uppercase">{mov.tipo}</TableCell>
                      <TableCell className="font-medium text-primary">{mov.nombreLugar}</TableCell>
                      <TableCell>
                        <Badge variant={isActive ? "default" : "secondary"} className={isActive ? "bg-success" : ""}>
                          {isActive ? "Adentro" : "Salió"}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-xs">
                        {format(new Date(mov.fechaEntrada), "d MMM, HH:mm", { locale: es })}
                      </TableCell>
                      <TableCell className="text-xs text-muted-foreground">
                        {mov.fechaSalida 
                          ? format(new Date(mov.fechaSalida), "d MMM, HH:mm", { locale: es })
                          : "---"}
                      </TableCell>
                    </TableRow>
                  )
                })
              )}
            </TableBody>
          </Table>
        </div>
      </CardContent>
    </Card>
  )
}