"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast"
import { movimientosApi } from "@/lib/api"
import { useAuth } from "@/contexts/auth-context"
import { LogIn, Loader2 } from "lucide-react"

interface VehicleEntryFormProps {
  onSuccess?: () => void
}

export function VehicleEntryForm({ onSuccess }: VehicleEntryFormProps) {
  const [placa, setPlaca] = useState("")
  const [nombreLugar, setNombreLugar] = useState("")
  const [tipo, setTipo] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const { user } = useAuth()
  const { toast } = useToast()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!user) return

    setIsLoading(true)

    const response = await movimientosApi.entrada({
      placa: placa.toUpperCase(),
      usuarioId: user.id,
      ...(nombreLugar && { nombreLugar }),
      ...(tipo && { tipo }),
    })

    if (response.error) {
      toast({
        title: "Error al registrar entrada",
        description: response.error,
        variant: "destructive",
      })
    } else {
      toast({
        title: "Entrada registrada",
        description: `Vehículo ${placa.toUpperCase()} registrado exitosamente`,
      })
      setPlaca("")
      setNombreLugar("")
      setTipo("")
      onSuccess?.()
    }

    setIsLoading(false)
  }

  return (
    <Card className="border-border">
      <CardHeader>
        <CardTitle className="flex items-center gap-2 text-card-foreground">
          <LogIn className="h-5 w-5 text-success" />
          Registrar Entrada
        </CardTitle>
        <CardDescription>Ingrese los datos del vehículo que ingresa al parqueadero</CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="placa">Placa del Vehículo *</Label>
            <Input
              id="placa"
              placeholder="ABC123"
              value={placa}
              onChange={(e) => setPlaca(e.target.value.toUpperCase())}
              required
              disabled={isLoading}
              className="uppercase"
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="tipo">Tipo de Vehículo</Label>
            <Select value={tipo} onValueChange={setTipo} disabled={isLoading}>
              <SelectTrigger>
                <SelectValue placeholder="Seleccione tipo (opcional)" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="CARRO">Carro</SelectItem>
                <SelectItem value="MOTO">Moto</SelectItem>
                <SelectItem value="BUS">Bus</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="nombreLugar">Lugar Específico (Opcional)</Label>
            <Input
              id="nombreLugar"
              placeholder="Ej: A-01"
              value={nombreLugar}
              onChange={(e) => setNombreLugar(e.target.value)}
              disabled={isLoading}
            />
            <p className="text-xs text-muted-foreground">Dejar vacío para asignación automática</p>
          </div>

          <Button
            type="submit"
            className="w-full bg-success text-success-foreground hover:bg-success/90"
            disabled={isLoading}
          >
            {isLoading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Registrando...
              </>
            ) : (
              <>
                <LogIn className="mr-2 h-4 w-4" />
                Registrar Entrada
              </>
            )}
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
