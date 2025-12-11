"use client"

import { useEffect, useState, useCallback } from "react"
import { useRouter } from "next/navigation"
import { useAuth } from "@/contexts/auth-context"
import { DashboardHeader } from "@/components/dashboard/header"
import { StatsCards, TypeStatsCards } from "@/components/dashboard/stats-cards"
import { VehicleFilter } from "@/components/dashboard/vehicle-filter"
import { VehicleEntryForm } from "@/components/admin/vehicle-entry-form"
import { VehicleExitForm } from "@/components/admin/vehicle-exit-form"
import { CreatePlaceForm } from "@/components/admin/create-place-form"
import { AllPlacesTable } from "@/components/admin/all-places-table"
import { lugaresApi, type Estadisticas, type Lugar } from "@/lib/api"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { RefreshCw, LayoutDashboard, Car, ParkingCircle } from "lucide-react"
import { Button } from "@/components/ui/button"

export default function AdminPage() {
  const { user, isLoading: authLoading, isAuthenticated } = useAuth()
  const router = useRouter()
  const [stats, setStats] = useState<Estadisticas | null>(null)
  const [allPlaces, setAllPlaces] = useState<Lugar[]>([])
  const [filter, setFilter] = useState("")
  const [isLoading, setIsLoading] = useState(true)

  const fetchData = useCallback(async () => {
    setIsLoading(true)
    try {
      const [statsRes, placesRes] = await Promise.all([
        lugaresApi.getEstadisticas(filter || undefined),
        lugaresApi.getAll(),
      ])

      if (statsRes.data) setStats(statsRes.data)
      if (placesRes.data) setAllPlaces(placesRes.data)
    } catch (error) {
      console.error("Error fetching data:", error)
    }
    setIsLoading(false)
  }, [filter])

  useEffect(() => {
    if (!authLoading) {
      if (!isAuthenticated) {
        router.push("/login")
      } else if (user?.rol !== "ADMIN") {
        router.push("/dashboard")
      }
    }
  }, [authLoading, isAuthenticated, user, router])

  useEffect(() => {
    if (isAuthenticated && user?.rol === "ADMIN") {
      fetchData()
    }
  }, [isAuthenticated, user, fetchData])

  if (authLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary" />
      </div>
    )
  }

  if (!isAuthenticated || user?.rol !== "ADMIN") {
    return null
  }

  return (
    <div className="min-h-screen bg-background">
      <DashboardHeader />

      <main className="container mx-auto px-4 py-8">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
          <div>
            <h2 className="text-3xl font-bold text-foreground">Panel de Administración</h2>
            <p className="text-muted-foreground">Gestiona el parqueadero, registra entradas y salidas de vehículos.</p>
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

        <Tabs defaultValue="overview" className="space-y-8">
          <TabsList className="bg-muted">
            <TabsTrigger value="overview" className="flex items-center gap-2">
              <LayoutDashboard className="h-4 w-4" />
              Resumen
            </TabsTrigger>
            <TabsTrigger value="movements" className="flex items-center gap-2">
              <Car className="h-4 w-4" />
              Movimientos
            </TabsTrigger>
            <TabsTrigger value="places" className="flex items-center gap-2">
              <ParkingCircle className="h-4 w-4" />
              Lugares
            </TabsTrigger>
          </TabsList>

          <TabsContent value="overview" className="space-y-8">
            <StatsCards stats={stats} isLoading={isLoading} />

            <div className="space-y-4">
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <h3 className="text-xl font-semibold text-foreground">Filtrar por tipo</h3>
                <VehicleFilter selected={filter} onSelect={setFilter} />
              </div>
              <TypeStatsCards stats={stats} isLoading={isLoading} />
            </div>
          </TabsContent>

          <TabsContent value="movements" className="space-y-8">
            <div className="grid gap-6 md:grid-cols-2">
              <VehicleEntryForm onSuccess={fetchData} />
              <VehicleExitForm onSuccess={fetchData} />
            </div>
          </TabsContent>

          <TabsContent value="places" className="space-y-8">
            <div className="grid gap-6 lg:grid-cols-3">
              <div className="lg:col-span-1">
                <CreatePlaceForm onSuccess={fetchData} />
              </div>
              <div className="lg:col-span-2">
                <AllPlacesTable places={allPlaces} isLoading={isLoading} />
              </div>
            </div>
          </TabsContent>
        </Tabs>
      </main>
    </div>
  )
}
