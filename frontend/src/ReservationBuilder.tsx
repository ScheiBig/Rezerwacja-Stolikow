import axios from 'axios';
import { ReactElement, useState } from 'react';
import { baseUrl } from './App';

import DateDisplay from './components/DateDisplay';
import RestaurantList from './components/RestaurantList';
import RestaurantMap from './components/RestaurantMap';
import WeightedCalendar from './components/WeightedCalendar';
import { useHeaderContext } from './context/HeaderContext';
import { restaurant } from './types';

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
    function acceptDate(date: Date) {

    }

    if (headerContext.getRestaurantID() === 0) {

        return <main className="flex flex-col justify-center s_lrg:justify-start h-full s_lrg:overflow-scroll">
            <RestaurantList onSelect={handleSelection} />
        </main>
    } else if (headerContext.getDate() === null) {
        return <main className="grid grid-cols-8 grid-rows-6 h-full">
            <WeightedCalendar className="col-span-5 row-span-6 s_mid:col-span-8 s_mid:row-span-5" forMonth={new Date()} weightTransform={transform} onSelect={d => setDate(d)} restaurantID={headerContext.getRestaurantID()} />
            <DateDisplay onAccept={d => headerContext.setDate(d)} className="col-span-3 row-span-6 s_mid:col-span-8 s_mid:row-span-1" date={date} />
        </main>
    } else if (headerContext.getTableTime() === null) {
        return <main className="h-full w-full grid grid-cols-8 grid-rows-6">
            <RestaurantMap className="h-full col-span-5 row-span-6" />
            <div className="col-span-3 row-span-6" />
        </main>
    } else {
        return <br />
    }
}
