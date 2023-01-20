import { createContext, useContext } from "react"
import { useQuery } from "react-query"
import { baseUrl } from "../App"
import { childProps, restaurant, restaurantsKey } from "../types"

export type restaurantsContext = {
    getRestaurants: () => (restaurant[] | undefined)
}

const RestaurantsContext = createContext({} as restaurantsContext)

export function useRestaurantsContext() { return useContext(RestaurantsContext) }

export function RestaurantsContextProvider({ children }: childProps) {
    const { data } = useQuery(restaurantsKey, async () => {
        const res = await fetch(`${baseUrl}/restaurants`)
        return (await res.json()) as restaurant[]
    }, {
        staleTime: 60 * 60 * 1000
    })

    function getRestaurants() { return data }

    return <RestaurantsContext.Provider value={{ getRestaurants }}>
        {children}
    </RestaurantsContext.Provider>
}
