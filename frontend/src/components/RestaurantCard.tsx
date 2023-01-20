import { ReactElement } from 'react';

import { baseUrl } from '../App';
import { restaurant } from '../types';

type propsT = {
    restaurant: restaurant,
    onClick: (restaurant: restaurant) => void
}

export default function RestaurantCard({ restaurant, onClick }: propsT): ReactElement {

    return <section className="cursor-pointer flex flex-col bg-zinc-50 hover:bg-zinc-200 dark:bg-zinc-900 dark:hover:bg-zinc-800 rounded-xl border border-zinc-300 dark:border-zinc-800 h-[35rem] s_lrg:h-[27rem] s_mid:h-[35rem] w-80 s_mid:w-[calc(100vw-2rem)] s_mid:mx-4 overflow-hidden mt-2" onClick={() => onClick(restaurant)}>
        <img className="h-80 s_lrg:h-48 s_mid:h-80 object-cover" src={`${baseUrl}${restaurant.image.slice(1)}`} />
        <h6 className="px-4 py-3 text-xl s_lrg:text-base font-bold">Restauracja "{restaurant.name}"</h6>
        <div className="grid grid-cols-2 justify-between px-6 opacity-80">
            <p>Poniedziałek:</p>
            <p className="text-right">{restaurant.openingHours.monday.from}-{restaurant.openingHours.monday.to}</p>
            <p>Wtorek:</p>
            <p className="text-right">{restaurant.openingHours.tuesday.from}-{restaurant.openingHours.tuesday.to}</p>
            <p>Środa:</p>
            <p className="text-right">{restaurant.openingHours.wednesday.from}-{restaurant.openingHours.wednesday.to}</p>
            <p>Czwartek:</p>
            <p className="text-right">{restaurant.openingHours.thursday.from}-{restaurant.openingHours.thursday.to}</p>
            <p>Piątek:</p>
            <p className="text-right">{restaurant.openingHours.friday.from}-{restaurant.openingHours.friday.to}</p>
            <p>Sobota:</p>
            <p className="text-right">{restaurant.openingHours.saturday.from}-{restaurant.openingHours.saturday.to}</p>
            <p>Niedziela:</p>
            <p className="text-right">{restaurant.openingHours.sunday.from}-{restaurant.openingHours.sunday.to}</p>
        </div>
    </section>
}
