const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "/api"

interface ApiResponse<T> {
  data?: T
  error?: string
  message?: string
}

async function fetchWithAuth<T>(endpoint: string, options: RequestInit = {}): Promise<ApiResponse<T>> {
  const token = typeof window !== "undefined" ? localStorage.getItem("accessToken") : null

  const headers: HeadersInit = {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
    ...options.headers,
  }

  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      ...options,
      headers,
    })

    const data = await response.json()

    if (!response.ok) {
      return {
        error: data.message || data.error || getErrorMessage(response.status),
      }
    }

    return { data }
  } catch (error) {
    return { error: "Error de conexión. Por favor, intente nuevamente." }
  }
}

function getErrorMessage(status: number): string {
  const messages: Record<number, string> = {
    400: "Solicitud inválida",
    401: "No autorizado. Por favor, inicie sesión nuevamente.",
    403: "Acceso denegado",
    404: "Recurso no encontrado",
    409: "El recurso ya existe o hay un conflicto",
    500: "Error del servidor. Intente más tarde.",
  }
  return messages[status] || "Ha ocurrido un error inesperado"
}

// Auth
export interface LoginCredentials {
  email: string
  password: string
}

export interface RegisterData {
  nombre: string
  email: string
  password: string
}

export interface AuthResponse {
  accessToken: string
  user: {
    id: number
    nombre: string
    email: string
    rol: "ADMIN" | "USER"
  }
}

export const authApi = {
  login: (credentials: LoginCredentials) =>
    fetchWithAuth<AuthResponse>("/auth/login", {
      method: "POST",
      body: JSON.stringify(credentials),
    }),

  register: (data: RegisterData) =>
    fetchWithAuth<AuthResponse>("/auth/registro", {
      method: "POST",
      body: JSON.stringify(data),
    }),
}

// Lugares
export interface Lugar {
  id: number
  nombre: string
  tipo: "CARRO" | "MOTO" | "BUS"
  estado: "LIBRE" | "OCUPADO"
}

export interface Estadisticas {
  total: number
  libres: number
  ocupados: number
  porTipo?: {
    CARRO: { libres: number; ocupados: number }
    MOTO: { libres: number; ocupados: number }
    BUS: { libres: number; ocupados: number }
  }
}

export const lugaresApi = {
  getEstadisticas: (tipo?: string) =>
    fetchWithAuth<Estadisticas>(`/lugares/estadisticas${tipo ? `?tipo=${tipo}` : ""}`),

  getLibres: (tipo?: string) => fetchWithAuth<Lugar[]>(`/lugares/libres${tipo ? `?tipo=${tipo}` : ""}`),

  getOcupados: (tipo?: string) => fetchWithAuth<Lugar[]>(`/lugares/ocupados${tipo ? `?tipo=${tipo}` : ""}`),

  getAll: () => fetchWithAuth<Lugar[]>("/lugares"),

  create: (data: { nombre: string; tipo: string }) =>
    fetchWithAuth<Lugar>("/lugares", {
      method: "POST",
      body: JSON.stringify(data),
    }),
}

// Movimientos
export interface Movimiento {
  id: number
  placa: string
  lugarId: number
  lugar?: Lugar
  usuarioId: number
  fechaEntrada: string
  fechaSalida?: string
}

export interface EntradaData {
  placa: string
  usuarioId: number
  nombreLugar?: string
  tipo?: string
}

export const movimientosApi = {
  entrada: (data: EntradaData) =>
    fetchWithAuth<Movimiento>("/movimientos/entrada", {
      method: "POST",
      body: JSON.stringify(data),
    }),

  salida: (placa: string) =>
    fetchWithAuth<Movimiento>(`/movimientos/salida/${placa}`, {
      method: "PUT",
    }),
}
