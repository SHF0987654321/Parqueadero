// app/login/page.tsx
"use client"

// 1. Asegúrate de importar useEffect
import React, { useState, useEffect } from "react" 

import { useRouter } from "next/navigation"
import Link from "next/link"
// 2. Aquí es donde se importa 'useAuth'
import { useAuth } from "@/contexts/auth-context" 
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast"
import { Car, Loader2 } from "lucide-react"

export default function LoginPage() {
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const { login, user, isAuthenticated } = useAuth() // Accede al 'user' tipado
  const router = useRouter()
  const { toast } = useToast()

  // --- CORRECCIÓN FINAL: Usar useEffect para la Redirección Inicial ---
  // Este hook se ejecuta cuando 'user' cambia (al cargar la página o después de un login exitoso).
  useEffect(() => {
    // Como 'user' es de tipo User | null (definido en auth-context.tsx), 
    // TypeScript permite acceder a 'user.rol' de forma segura.
    if (user && isAuthenticated) {
      // TypeScript ya sabe que user.rol existe.
      const destination = user.rol === "ADMIN" ? "/admin" : "/dashboard"
      router.push(destination)
    }
  }, [user, isAuthenticated, router]) 

  // Si el usuario existe, se retorna 'null' para que React no intente renderizar
  // el formulario mientras la redirección está en curso.
  if (user && isAuthenticated) {
    return null
  }
  
  // --- LÓGICA DE SUBMIT ---
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)

    const result = await login({ email, password })

    if (result.success) {
      toast({
        title: "Bienvenido",
        description: "Has iniciado sesión correctamente",
      })
      
      // La redirección aquí se hará automáticamente gracias al 'useEffect'
      // que detectará que la variable 'user' cambió después del login.
      // Ya no necesitamos la lógica de 'localStorage.getItem("user")' aquí.

    } else {
      toast({
        title: "Error",
        description: result.error || "Credenciales inválidas",
        variant: "destructive",
      })
    }

    setIsLoading(false)
  }

  // --- RENDERIZADO DEL FORMULARIO ---
  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-4">
      {/* ... (El resto del JSX es el mismo) */}
      <div className="w-full max-w-md">
        <div className="flex flex-col items-center mb-8">
          <div className="flex items-center gap-3 mb-2">
            <div className="p-3 bg-primary rounded-xl">
              <Car className="h-8 w-8 text-primary-foreground" />
            </div>
            <h1 className="text-2xl font-bold text-foreground">Parqueadero</h1>
          </div>
          <p className="text-muted-foreground text-center">Universidad del Pacífico</p>
        </div>

        <Card className="border-border shadow-lg">
          <CardHeader className="space-y-1">
            <CardTitle className="text-2xl text-center">Iniciar Sesión</CardTitle>
            <CardDescription className="text-center">Ingresa tus credenciales para acceder al sistema</CardDescription>
          </CardHeader>
          <form onSubmit={handleSubmit}>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="email">Correo Electrónico</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="correo@unipacifico.edu.co"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  disabled={isLoading}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="password">Contraseña</Label>
                <Input
                  id="password"
                  type="password"
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  disabled={isLoading}
                />
              </div>
            </CardContent>
            <CardFooter className="flex flex-col gap-4">
              <Button type="submit" className="w-full bg-primary hover:bg-primary/90" disabled={isLoading}>
                {isLoading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Ingresando...
                  </>
                ) : (
                  "Iniciar Sesión"
                )}
              </Button>
              <p className="text-sm text-muted-foreground text-center">
                ¿No tienes cuenta?{" "}
                <Link href="/registro" className="text-primary hover:underline font-medium">
                  Regístrate aquí
                </Link>
              </p>
            </CardFooter>
          </form>
        </Card>
      </div>
    </div>
  )
}