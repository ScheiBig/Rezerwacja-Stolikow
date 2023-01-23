import axios, { AxiosError } from 'axios';
import { ReactElement, useState } from 'react';
import { baseUrl, routing } from './App';

import DateDisplay from './components/DateDisplay';
import RestaurantList from './components/RestaurantList';
import RestaurantMap from './components/RestaurantMap';
import WeightedCalendar from './components/WeightedCalendar';
import QueryDisplay from './components/QueryDisplay';
import { useHeaderContext } from './context/HeaderContext';
import { restaurant, toJavaDate } from './types';
import { useNavigate } from 'react-router';
import Spinner from './components/Spinner';
import useLocalStorage from './hooks/useLocalStorage';
import { useLockContext } from './context/LockContext';

const defWeights = [
    1, 1, 1, 1, 1,
    1, 1, 1, 1, 1,
    1, 1, 1, 1, 1,
    1, 1, 1, 1, 1,
    1, 1, 1, 1, 1,
    1, 1, 1, 1, 1,
    1
]

function transform(w: number) {
    if (0 <= w && w < 0.25) {
        return "bg-green-200 dark:bg-green-800"
    }
    else if (0.25 <= w && w < 0.50) {
        return "bg-orange-200 dark:bg-orange-800"
    }
    else if (0.50 <= w && w <= 0.75) {
        return "bg-red-200 dark:bg-red-80"
    } else {
        return "bg-zinc-500"
    }
}

export default function ReservationBuilder(): ReactElement {

    const headerContext = useHeaderContext()
    function handleSelection(r: restaurant) {
        headerContext.setRestaurantID(r.ID)
    }

    const [date, setDate] = useState<Date | null>(null)
    
    const routeTo = useNavigate()
    const [isLoading, setIsLoading] = useState(false)
    const { lock, setLock } = useLockContext()

    async function acceptDetails() {
        setIsLoading(true)
        try {
            const resp = await axios.put<string>(`${baseUrl}/dining_tables/locks`, {
                diningTable: {
                    restaurantID: headerContext.restaurantID,
                    number: headerContext.diningTable
                },
                bounds: {
                    from: toJavaDate(headerContext.dateTime || new Date(0)),
                    durationH: headerContext.duration
                }
            })
            routeTo(routing.accept_reservation, {state: resp.data})
        }
        catch (e) {
            window.alert("Ktoś inny już wykonał rezerwację na ten stolik! Wybierz inny stolik, lub spróbuj ponownie później")
            setIsLoading(false)
        }
    }

    if (headerContext.restaurantID === 0) {
        
        return <main className="flex flex-col justify-center s_lrg:justify-start h-full s_lrg:overflow-scroll">
            <RestaurantList onSelect={handleSelection} />
        </main>
    } else if (headerContext.date === null) {
        
        return <main className="grid grid-cols-8 grid-rows-6 h-full">
            <WeightedCalendar className="col-span-5 row-span-6 s_mid:col-span-8 s_mid:row-span-5" forMonth={new Date()} weightTransform={transform} onSelect={d => setDate(d)} restaurantID={headerContext.restaurantID} />
            <DateDisplay onAccept={d => { headerContext.setDate(d); setDate(null) } } className="col-span-3 row-span-6 s_mid:col-span-8 s_mid:row-span-1" date={date} />
        </main>
    } else if (!isLoading) {
    
        return <main className="h-full w-full grid grid-cols-8 grid-rows-6">
            <RestaurantMap className="h-full col-span-5 row-span-6 s_mid:col-span-8 s_mid:row-span-5" />
            <QueryDisplay onAccept={acceptDetails} className="col-span-3 row-span-6  s_mid:col-span-8 s_mid:row-span-1" />
        </main>
    } else {
        return <Spinner className="h-full w-full" />
    }
}
