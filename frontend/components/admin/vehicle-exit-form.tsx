"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast"
import { movimientosApi } from "@/lib/api"
import { LogOut, Loader2 } from "lucide-react"

interface VehicleExitFormProps {
  onSuccess?: () => void
}

export function VehicleExitForm({ onSuccess }: VehicleExitFormProps) {
  const [placa, setPlaca] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const { toast } = useToast()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    setIsLoading(true)

    const response = await movimientosApi.salida(placa.toUpperCase())

    if (response.error) {
      toast({
        title: "Error al registrar salida",
        description: response.error,
        variant: "destructive",
      })
    } else {
      toast({
        title: "Salida registrada",
        description: `Vehículo ${placa.toUpperCase()} ha salido del parqueadero`,
      })
      setPlaca("")
      onSuccess?.()
    }

    setIsLoading(false)
  }

  return (
    <Card className="border-border">
      <CardHeader>
        <CardTitle className="flex items-center gap-2 text-card-foreground">
          <LogOut className="h-5 w-5 text-destructive" />
          Registrar Salida
        </CardTitle>
        <CardDescription>Ingrese la placa del vehículo que sale del parqueadero</CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="placa-salida">Placa del Vehículo</Label>
            <Input
              id="placa-salida"
              placeholder="ABC123"
              value={placa}
              onChange={(e) => setPlaca(e.target.value.toUpperCase())}
              required
              disabled={isLoading}
              className="uppercase"
            />
          </div>

          <Button
            type="submit"
            className="w-full bg-destructive text-destructive-foreground hover:bg-destructive/90"
            disabled={isLoading}
          >
            {isLoading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Procesando...
              </>
            ) : (
              <>
                <LogOut className="mr-2 h-4 w-4" />
                Registrar Salida
              </>
            )}
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
