"use client"

import { createContext, useContext, useState, useEffect, useCallback, type ReactNode } from "react"
import { authApi, type LoginCredentials, type RegisterData } from "@/lib/api"

interface User {
  id: number
  nombre: string
  email: string
  rol: "ADMIN" | "USUARIO"
}

interface AuthContextType {
  user: User | null
  isLoading: boolean
  isAuthenticated: boolean
  login: (credentials: LoginCredentials) => Promise<{ success: boolean; error?: string }>
  register: (data: RegisterData) => Promise<{ success: boolean; error?: string }>
  logout: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem("accessToken")
    const storedUser = localStorage.getItem("user")

    if (token && storedUser) {
      try {
        setUser(JSON.parse(storedUser))
      } catch {
        localStorage.removeItem("accessToken")
        localStorage.removeItem("user")
      }
    }
    setIsLoading(false)
  }, [])

  const login = useCallback(async (credentials: LoginCredentials) => {
    const response = await authApi.login(credentials)

    if (response.error) {
      return { success: false, error: response.error }
    }

    if (response.data) {
      localStorage.setItem("accessToken", response.data.accessToken)
      localStorage.setItem("user", JSON.stringify(response.data.user))
      setUser(response.data.user)
      return { success: true }
    }

    return { success: false, error: "Error desconocido" }
  }, [])

  const register = useCallback(async (data: RegisterData) => {
    const response = await authApi.register(data)

    if (response.error) {
      return { success: false, error: response.error }
    }

    if (response.data) {
      localStorage.setItem("accessToken", response.data.accessToken)
      localStorage.setItem("user", JSON.stringify(response.data.user))
      setUser(response.data.user)
      return { success: true }
    }

    return { success: false, error: "Error desconocido" }
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem("accessToken")
    localStorage.removeItem("user")
    setUser(null)
  }, [])

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoading,
        isAuthenticated: !!user,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}
