import { ReactElement, useEffect, useId, useState } from "react";
import { useHeaderContext } from "../context/HeaderContext";
import { useRestaurantsContext } from "../context/RestaurantsContext";
import { outline$tyle } from "../types";
import CounterInput from "./CounterInput";
import TriCheckbox from "./TriCheckbox";

type propsT = {
    className?: string,
    onAccept: () => void
}

export default function QueryDisplay({ className, onAccept }: propsT): ReactElement {

    const headerContext = useHeaderContext()
    const restaurants = useRestaurantsContext()

    const restaurant = restaurants.getRestaurants()?.find(r => r.ID === headerContext.restaurantID)

    const openingHours = (() => {
        switch (headerContext.date?.getDay()) {
            case 1: return restaurant?.openingHours.monday
            case 2: return restaurant?.openingHours.tuesday
            case 3: return restaurant?.openingHours.wednesday
            case 4: return restaurant?.openingHours.thursday
            case 5: return restaurant?.openingHours.friday
            case 6: return restaurant?.openingHours.saturday
            case 0: return restaurant?.openingHours.sunday
            default: return
        }
    })()!

    const openFrom = Number.parseInt(openingHours.from.slice(0, 2))
    const to = Number.parseInt(openingHours.to.slice(0, 2))
    const openTo = to > openFrom ? to : to + 24



    const t = headerContext.time
    const setTime = headerContext.setTime
    const time = t || openFrom
    useEffect(() => { if (t !== time) setTime(time) })

    const d = headerContext.duration
    const setDuration = headerContext.setDuration
    const duration = d > 0 ? d : 1
    const maxDuration = openTo - time
    useEffect(() => {
        if (d === 0) setDuration(duration)
        if (duration > maxDuration) setDuration(maxDuration)
    }, [d, duration, maxDuration, setDuration])


    return <aside className={`${className} h-full bg-violet-400 dark:bg-violet-900 s_mid:flex flex-row p-4 s_mid:py-0  relative`}>
        <div className="border-b-2 border-zinc-500 flex flex-col py-2">
            <p className="col-span-2 text-2xl font-semibold s_mid:hidden py-2">Czas rezerwacji</p>
            <div className="flex flex-row s_lmd:flex-col gap-4 s_lrg:gap-1 s_mid:flex-row">
                <p className="text-opacity-80 flex flex-row s_lrg:flex-col gap-2 s_lrg:gap-1">Godzina:
                    <CounterInput min={openFrom} max={openTo - 1} value={time} onChange={setTime} transformation={v => v < 24 ? `${v.toString().padStart(2, "0")}:00` : `↩${(v - 24).toString().padStart(2, "0")}:00`} />
                </p>
                <p className="text-opacity-80 flex flex-row s_lrg:flex-col gap-2 s_lrg:gap-1">Długość:
                <CounterInput min={1} max={maxDuration} value={duration} onChange={setDuration} transformation={v => `${v} h`} /></p>
            </div>
        </div>
        <div className="py-2 w-40 s_mid:flex flex-row gap-4 s_mid:pl-2">
            <p className="s_mid:hidden text-2xl font-semibold py-2">Opcje stolika:</p>
            <span className="flex flex-row items-center"><span className="s_mid:hidden pr-3 grow">Przy oknie:</span><span className="hidden s_mid:inline pr-3">🪟</span><TriCheckbox checked={headerContext.tableQuery?.byWindow} onChange={v => { headerContext.setTableQuery(q => q ? { ...q, byWindow: v } : { byWindow: v }); headerContext.setDiningTable(0) }} /></span>
            <span className="flex flex-row items-center"><span className="s_mid:hidden pr-3 grow">Na zewnątrz:</span><span className="hidden s_mid:inline pr-3">🌞</span><TriCheckbox checked={headerContext.tableQuery?.outside} onChange={v => { headerContext.setTableQuery(q => q ? { ...q, outside: v } : { outside: v }); headerContext.setDiningTable(0) }} /></span>
            <span className="flex flex-row items-center"><span className="s_mid:hidden pr-3 grow">Dla palących:</span><span className="hidden s_mid:inline pr-3">🚬</span><TriCheckbox checked={headerContext.tableQuery?.smokingAllowed} onChange={v => { headerContext.setTableQuery(q => q ? { ...q, smokingAllowed: v } : { smokingAllowed: v }); headerContext.setDiningTable(0) }} /></span>
        </div>
        {headerContext.time !== null &&
            headerContext.duration !== 0 &&
            headerContext.diningTable !== 0 &&
            <button onClick={onAccept} className={`${outline$tyle} bg-lime-600 h-12 w-12 rounded-full row-start-4 sh_shr:row-start-3 s_mid:row-auto place-self-end s_mid:place-self-auto m-8 s_mid:m-0 absolute bottom-0 right-0 s_mid:bottom-3 s_mid:right-3`}>✓</button>}
    </aside>
}
