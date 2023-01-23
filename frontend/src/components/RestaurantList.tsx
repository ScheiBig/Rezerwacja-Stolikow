import { ReactElement } from 'react';

import { useRestaurantsContext } from '../context/RestaurantsContext';
import { restaurant } from '../types';
import RestaurantCard from './RestaurantCard';
import Spinner from './Spinner';

type propsT = {
    onSelect: (restaurant: restaurant) => void
}

export default function RestaurantList({ onSelect }: propsT): ReactElement {

    const restaurants = useRestaurantsContext().getRestaurants()

    return <>{
        restaurants ? <section className="flex flex-row flex-wrap s_mid:flex-col justify-evenly items-start">
            {restaurants.map(r => <RestaurantCard key={r.ID} restaurant={r} onClick={onSelect} />)}
        </section>
            : <Spinner className="h-full w-full" />
    } </>
}
