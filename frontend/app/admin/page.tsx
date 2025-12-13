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

// Importar los nuevos componentes y la API de movimientos ampliada
import { MovementsTable } from "@/components/admin/movements-table" // <--- NUEVO
import { FilterButtons } from "@/components/admin/filter-buttons" // <--- NUEVO
import { lugaresApi, movimientosApi, type Estadisticas, type Lugar, type Movimiento } from "@/lib/api"
// ---------------------------------------------------------------

import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { RefreshCw, LayoutDashboard, Car, ParkingCircle, Clock } from "lucide-react" // Añadido Clock
import { Button } from "@/components/ui/button"

// Tipos para el filtro de movimientos
type MovementFilterType = "ACTIVE" | "HISTORY" | "ALL"

export default function AdminPage() {
  const { user, isLoading: authLoading, isAuthenticated } = useAuth()
  const router = useRouter()
  const [stats, setStats] = useState<Estadisticas | null>(null)
  const [allPlaces, setAllPlaces] = useState<Lugar[]>([])
  
  // ESTADOS PARA MOVIMIENTOS
  const [allMovements, setAllMovements] = useState<Movimiento[]>([]) // Lista completa
  const [movementFilter, setMovementFilter] = useState<MovementFilterType>("ACTIVE") // Filtro de tabla
  // -------------------------

  const [filter, setFilter] = useState("")
  const [isLoading, setIsLoading] = useState(true)

  const fetchData = useCallback(async () => {
    setIsLoading(true)
    try {
      const [statsRes, placesRes, movementsRes] = await Promise.all([ // <-- AÑADIDO movementsRes
        lugaresApi.getEstadisticas(filter || undefined),
        lugaresApi.getAll(),
        movimientosApi.getHistorial(), // <-- NUEVA LLAMADA a /api/movimientos
      ])

      if (statsRes.data) setStats(statsRes.data)
      if (placesRes.data) setAllPlaces(placesRes.data)
      if (movementsRes.data) setAllMovements(movementsRes.data) // <-- GUARDAR DATOS
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

  // LÓGICA DEL FILTRO DE MOVIMIENTOS
  const filteredMovements = allMovements.filter(mov => {
    if (movementFilter === "ACTIVE") {
      // Movimiento activo: fechaSalida es null (parqueado)
      return mov.fechaSalida === null
    }
    if (movementFilter === "HISTORY") {
      // Movimiento histórico: fechaSalida NO es null (ya salió)
      return mov.fechaSalida !== null
    }
    return true // ALL
  })
  
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

          {/* === TABS CONTENT MOVEMENTS (AQUÍ ESTÁ EL CAMBIO) === */}
          <TabsContent value="movements" className="space-y-8">
            {/* Formulario de Entrada y Salida */}
            <div className="grid gap-6 md:grid-cols-2">
              <VehicleEntryForm onSuccess={fetchData} />
              <VehicleExitForm onSuccess={fetchData} />
            </div>
            
            {/* Filtro y Tabla de Movimientos */}
            <div className="space-y-4">
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <h3 className="text-xl font-semibold text-foreground flex items-center gap-2">
                    <Clock className="h-5 w-5 text-primary"/> Registro de Movimientos
                </h3>
                {/* 1. Botones de Filtro (Activo / Historial / Todos) */}
                <FilterButtons 
                    currentFilter={movementFilter} 
                    onSelect={setMovementFilter} 
                />
              </div>
              
              {/* 2. Tabla de Movimientos Filtrados */}
              <MovementsTable 
                  data={filteredMovements} 
                  isLoading={isLoading} 
              />
            </div>
          </TabsContent>
          {/* ====================================================== */}

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
