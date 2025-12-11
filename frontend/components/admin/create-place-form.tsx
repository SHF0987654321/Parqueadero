"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast"
import { lugaresApi } from "@/lib/api"
import { PlusCircle, Loader2 } from "lucide-react"

interface CreatePlaceFormProps {
  onSuccess?: () => void
}

export function CreatePlaceForm({ onSuccess }: CreatePlaceFormProps) {
  const [nombre, setNombre] = useState("")
  const [tipo, setTipo] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const { toast } = useToast()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!tipo) {
      toast({
        title: "Error",
        description: "Seleccione un tipo de lugar",
        variant: "destructive",
      })
      return
    }

    setIsLoading(true)

    const response = await lugaresApi.create({ nombre, tipo })

    if (response.error) {
      toast({
        title: "Error al crear lugar",
        description: response.error,
        variant: "destructive",
      })
    } else {
      toast({
        title: "Lugar creado",
        description: `El lugar ${nombre} ha sido creado exitosamente`,
      })
      setNombre("")
      setTipo("")
      onSuccess?.()
    }

    setIsLoading(false)
  }

  return (
    <Card className="border-border">
      <CardHeader>
        <CardTitle className="flex items-center gap-2 text-card-foreground">
          <PlusCircle className="h-5 w-5 text-primary" />
          Crear Nuevo Lugar
        </CardTitle>
        <CardDescription>Agregue un nuevo espacio de estacionamiento al sistema</CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="nombre-lugar">Nombre del Lugar</Label>
            <Input
              id="nombre-lugar"
              placeholder="Ej: A-01, B-15, MOTO-03"
              value={nombre}
              onChange={(e) => setNombre(e.target.value)}
              required
              disabled={isLoading}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="tipo-lugar">Tipo de Lugar</Label>
            <Select value={tipo} onValueChange={setTipo} disabled={isLoading}>
              <SelectTrigger>
                <SelectValue placeholder="Seleccione tipo" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="CARRO">Carro</SelectItem>
                <SelectItem value="MOTO">Moto</SelectItem>
                <SelectItem value="BUS">Bus</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <Button
            type="submit"
            className="w-full bg-primary text-primary-foreground hover:bg-primary/90"
            disabled={isLoading}
          >
            {isLoading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Creando...
              </>
            ) : (
              <>
                <PlusCircle className="mr-2 h-4 w-4" />
                Crear Lugar
              </>
            )}
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
