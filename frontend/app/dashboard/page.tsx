"use client"

import { useEffect, useState, useCallback } from "react"
import { useRouter } from "next/navigation"
import { useAuth } from "@/contexts/auth-context"
import { DashboardHeader } from "@/components/dashboard/header"
import { StatsCards, TypeStatsCards } from "@/components/dashboard/stats-cards"
import { VehicleFilter } from "@/components/dashboard/vehicle-filter"
import { PlacesTable } from "@/components/dashboard/places-table"
import { lugaresApi, type Estadisticas, type Lugar } from "@/lib/api"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { RefreshCw } from "lucide-react"
import { Button } from "@/components/ui/button"

export default function DashboardPage() {
  const { user, isLoading: authLoading, isAuthenticated } = useAuth()
  const router = useRouter()
  const [stats, setStats] = useState<Estadisticas | null>(null)
  const [freePlaces, setFreePlaces] = useState<Lugar[]>([])
  const [occupiedPlaces, setOccupiedPlaces] = useState<Lugar[]>([])
  const [filter, setFilter] = useState("")
  const [isLoading, setIsLoading] = useState(true)

  const fetchData = useCallback(async () => {
    setIsLoading(true)
    try {
      const [statsRes, freeRes, occupiedRes] = await Promise.all([
        lugaresApi.getEstadisticas(filter || undefined),
        lugaresApi.getLibres(filter || undefined),
        lugaresApi.getOcupados(filter || undefined),
      ])

      if (statsRes.data) setStats(statsRes.data)
      if (freeRes.data) setFreePlaces(freeRes.data)
      if (occupiedRes.data) setOccupiedPlaces(occupiedRes.data)
    } catch (error) {
      console.error("Error fetching data:", error)
    }
    setIsLoading(false)
  }, [filter])

  useEffect(() => {
    if (!authLoading && !isAuthenticated) {
      router.push("/login")
    }
  }, [authLoading, isAuthenticated, router])

  useEffect(() => {
    if (isAuthenticated) {
      fetchData()
    }
  }, [isAuthenticated, fetchData])

  if (authLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary" />
      </div>
    )
  }

  if (!isAuthenticated) {
    return null
  }

  return (
    <div className="min-h-screen bg-background">
      <DashboardHeader />

      <main className="container mx-auto px-4 py-8">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
          <div>
            <h2 className="text-3xl font-bold text-foreground">Dashboard</h2>
            <p className="text-muted-foreground">
              Bienvenido, {user?.nombre}. Aquí está el estado actual del parqueadero.
            </p>
          </div>
          <Button
            variant="outline"
            size="sm"
            onClick={fetchData}
            disabled={isLoading}
            className="border-border bg-transparent"
          >
            <RefreshCw className={`mr-2 h-4 w-4 ${isLoading ? "animate-spin" : ""}`} />
            Actualizar
          </Button>
        </div>

        <div className="space-y-8">
          <StatsCards stats={stats} isLoading={isLoading} />

          <div className="space-y-4">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
              <h3 className="text-xl font-semibold text-foreground">Filtrar por tipo</h3>
              <VehicleFilter selected={filter} onSelect={setFilter} />
            </div>
            <TypeStatsCards stats={stats} isLoading={isLoading} />
          </div>

          <Tabs defaultValue="libres" className="space-y-4">
            <TabsList className="bg-muted">
              <TabsTrigger value="libres">Lugares Libres</TabsTrigger>
              <TabsTrigger value="ocupados">Lugares Ocupados</TabsTrigger>
            </TabsList>
            <TabsContent value="libres">
              <PlacesTable
                places={freePlaces}
                isLoading={isLoading}
                title="Lugares Disponibles"
                emptyMessage="No hay lugares libres disponibles"
              />
            </TabsContent>
            <TabsContent value="ocupados">
              <PlacesTable
                places={occupiedPlaces}
                isLoading={isLoading}
                title="Lugares Ocupados"
                emptyMessage="No hay lugares ocupados"
              />
            </TabsContent>
          </Tabs>
        </div>
      </main>
    </div>
  )
}
