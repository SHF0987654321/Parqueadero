"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Car, Bike, Bus, ParkingCircle, ParkingCircleOff } from "lucide-react"
import type { Estadisticas } from "@/lib/api"

interface StatsCardsProps {
  stats: Estadisticas | null
  isLoading: boolean
}

export function StatsCards({ stats, isLoading }: StatsCardsProps) {
  if (isLoading) {
    return (
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {[...Array(4)].map((_, i) => (
          <Card key={i} className="animate-pulse">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <div className="h-4 w-24 bg-muted rounded" />
              <div className="h-8 w-8 bg-muted rounded" />
            </CardHeader>
            <CardContent>
              <div className="h-8 w-16 bg-muted rounded" />
            </CardContent>
          </Card>
        ))}
      </div>
    )
  }

  const cards = [
    {
      title: "Total Lugares",
      value: stats?.total ?? 0,
      icon: ParkingCircle,
      color: "text-primary",
      bg: "bg-primary/10",
    },
    {
      title: "Lugares Libres",
      value: stats?.libres ?? 0,
      icon: ParkingCircle,
      color: "text-success",
      bg: "bg-success/10",
    },
    {
      title: "Lugares Ocupados",
      value: stats?.ocupados ?? 0,
      icon: ParkingCircleOff,
      color: "text-destructive",
      bg: "bg-destructive/10",
    },
    {
      title: "Ocupación",
      value: stats?.total ? `${Math.round((stats.ocupados / stats.total) * 100)}%` : "0%",
      icon: Car,
      color: "text-warning",
      bg: "bg-warning/10",
    },
  ]

  return (
    <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
      {cards.map((card) => (
        <Card key={card.title} className="border-border">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">{card.title}</CardTitle>
            <div className={`p-2 rounded-lg ${card.bg}`}>
              <card.icon className={`h-5 w-5 ${card.color}`} />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-card-foreground">{card.value}</div>
          </CardContent>
        </Card>
      ))}
    </div>
  )
}

export function TypeStatsCards({ stats, isLoading }: StatsCardsProps) {
  if (isLoading || !stats?.porTipo) {
    return null
  }

  const typeCards = [
    {
      title: "Carros",
      libres: stats.porTipo.CARRO?.libres ?? 0,
      ocupados: stats.porTipo.CARRO?.ocupados ?? 0,
      icon: Car,
      color: "text-primary",
    },
    {
      title: "Motos",
      libres: stats.porTipo.MOTO?.libres ?? 0,
      ocupados: stats.porTipo.MOTO?.ocupados ?? 0,
      icon: Bike,
      color: "text-accent",
    },
    {
      title: "Buses",
      libres: stats.porTipo.BUS?.libres ?? 0,
      ocupados: stats.porTipo.BUS?.ocupados ?? 0,
      icon: Bus,
      color: "text-warning",
    },
  ]

  return (
    <div className="grid gap-4 md:grid-cols-3">
      {typeCards.map((card) => (
        <Card key={card.title} className="border-border">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
              <card.icon className={`h-4 w-4 ${card.color}`} />
              {card.title}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex justify-between items-center">
              <div>
                <p className="text-xs text-muted-foreground">Libres</p>
                <p className="text-2xl font-bold text-success">{card.libres}</p>
              </div>
              <div className="h-12 w-px bg-border" />
              <div>
                <p className="text-xs text-muted-foreground">Ocupados</p>
                <p className="text-2xl font-bold text-destructive">{card.ocupados}</p>
              </div>
            </div>
          </CardContent>
        </Card>
      ))}
    </div>
  )
}
