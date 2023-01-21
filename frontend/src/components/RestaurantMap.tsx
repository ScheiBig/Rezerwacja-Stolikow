import axios from "axios";
import { ReactElement } from "react";
import { useQuery } from "react-query";
import { baseUrl } from "../App";
import { useHeaderContext } from "../context/HeaderContext";
import { useRestaurantsContext } from "../context/RestaurantsContext";
import { diningTableDetails } from "../types";

type propsT = {
    className?: string
}

export default function RestaurantMap({ className }: propsT): ReactElement {

    const restaurants = useRestaurantsContext()
    const headerContext = useHeaderContext()
    const restaurant = restaurants.getRestaurants()?.find(r => r.ID === headerContext.getRestaurantID())

    const restaurantMapping = useQuery(`${restaurant?.ID}mapping`, async () => {
        return (await axios.post<diningTableDetails[]>(`${baseUrl}/dining_tables/search/${restaurant?.ID}`)).data
    })

    return <div className={`${className} overflow-auto block`}>
        <div className="relative">
            <img className="max-w-max" src={`${baseUrl}${restaurant?.map.slice(1)}`} />
            {restaurantMapping.data && restaurantMapping.data.map(dt => {
                return <button className="absolute border-2 rounded-3xl bg-blue-400/50 text-2xl" style={{
                    bottom: dt.mapLocation.y,
                    left: dt.mapLocation.x,
                    height: dt.mapLocation.h,
                    width: dt.mapLocation.w
                }} type="button">{`${dt.number.toString()}${dt.byWindow ? " 🪟" : ""}${dt.outside ? " 🌞" : ""}${dt.smokingAllowed ? "" : " 🚭"}`}</button>
            })}
        </div>
    </div>
}
