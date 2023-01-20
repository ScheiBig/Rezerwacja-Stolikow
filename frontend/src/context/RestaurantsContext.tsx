import { createContext, useContext } from "react"
import { useQuery } from "react-query"
import { baseUrl } from "../App"
import { childProps, restaurant, restaurantsKey } from "../types"
import axios from "axios";

export type restaurantsContext = {
    getRestaurants: () => (restaurant[] | undefined)
}

const RestaurantsContext = createContext({} as restaurantsContext)

export function useRestaurantsContext() { return useContext(RestaurantsContext) }

export function RestaurantsContextProvider({ children }: childProps) {
    const { data } = useQuery(restaurantsKey, async () => {
        return (await axios.get<restaurant[]>(`${baseUrl}/restaurants`)).data
    }, {
        staleTime: 60 * 60 * 1000
    })

    function getRestaurants() { return data }

    return <RestaurantsContext.Provider value={{ getRestaurants }}>
        {children}
    </RestaurantsContext.Provider>
}
