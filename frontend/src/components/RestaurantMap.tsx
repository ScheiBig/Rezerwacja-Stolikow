import axios from "axios";
import { ReactElement, useState } from "react";
import { useQuery } from "react-query";
import { baseUrl } from "../App";
import { useHeaderContext } from "../context/HeaderContext";
import { useRestaurantsContext } from "../context/RestaurantsContext";
import { diningTable, diningTableDetails, toJavaDate } from "../types";
import Spinner from "./Spinner";

type propsT = {
    className?: string
}

export default function RestaurantMap({ className }: propsT): ReactElement {

    const restaurants = useRestaurantsContext()
    const headerContext = useHeaderContext()
    const restaurant = restaurants.getRestaurants()?.find(r => r.ID === headerContext.restaurantID)
    const restaurantMapping = useQuery(`${restaurant?.ID}mapping`, async () => {
        return (await axios.post<diningTableDetails[]>(`${baseUrl}/dining_tables/search/${restaurant?.ID}`)).data
    })

    const h = headerContext
    const key = `${h.dateTime}${h.duration}${h.diningTable}${h.restaurantID}${JSON.stringify(h.tableQuery)}`
    const queryMapping = useQuery(`${key}mapping`, async () => {
        return (await axios.post<diningTable[]>(`${baseUrl}/restaurants/${restaurant?.ID}/reservations/search`, {
            date: headerContext.dateTime && toJavaDate(headerContext.dateTime!),
            durationH: headerContext.duration,
            filter: headerContext.tableQuery || undefined
        })).data
    }, {
        cacheTime: 0
    })
    const tables = queryMapping.data && restaurantMapping.data?.map<[diningTableDetails, boolean | null]>(t => [t, headerContext.diningTable === t.number ? null : !!queryMapping.data?.find(d => d.number === t.number)]
    )

    const [spinny, setSpinny] = useState(true)

    return <div className={`${className} relative`}>
        {(spinny || queryMapping.isFetching) && <Spinner className="absolute top-0 left-0 h-full w-full"/>}
        <div className={`h-full overflow-auto block`}>
            <div className="relative">
                <img className="max-w-max brightness-75" src={`${baseUrl}${restaurant?.map.slice(1)}`} onLoad={() => setSpinny(false)} />
                {tables && tables.map(dt => {
                    return <button key={`${dt[0].restaurantID}_${dt[0].number}`} className={`absolute border-2 rounded-3xl ${dt[1] === null ? "bg-sky-400/75 dark:bg-sky-900/75" : "bg-emerald-400/50 dark:bg-emerald-900/50 hover:bg-emerald-400/75 hover:dark:bg-emerald-900/75 disabled:bg-red-400/50 dark:disabled:bg-red-900/50"} dark:text-white text-2xl`} style={{
                        bottom: dt[0].mapLocation.y,
                        left: dt[0].mapLocation.x,
                        height: dt[0].mapLocation.h,
                        width: dt[0].mapLocation.w
                    }} type="button" onClick={() => headerContext.setDiningTable(dt[0].number)} disabled={dt[1] === false}>
                        {`${dt[0].number.toString()}${dt[0].byWindow ? " 🪟" : ""}${dt[0].outside ? " 🌞" : ""}${dt[0].smokingAllowed ? " 🚬" : ""}`}
                    </button>
                })}
            </div>
        </div>
    </div>
}
