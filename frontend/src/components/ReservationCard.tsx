import axios from 'axios';
import { ReactElement } from 'react';

import { baseUrl } from '../App';
import { useRestaurantsContext } from '../context/RestaurantsContext';
import { dateTimeFormat, reservation, restaurant, timeFormat } from '../types';

type propsT = {
    reservation: reservation,
    onCancel: (reservation: reservation) => void
}

export default function ReservationCard({ reservation, onCancel }: propsT): ReactElement {

    const restaurantsContext = useRestaurantsContext()
    const restaurants = restaurantsContext.getRestaurants() || [] as restaurant[]
    const restaurant = restaurants.find((r) => r.ID === reservation.diningTable.restaurantID)
    const date = new Date(reservation.bounds.from)
    const toDate = new Date(date.toISOString())
    toDate.setHours((date.getHours() + reservation.bounds.durationH) % 24)

    async function handleCancellation() {
        const sure = window.confirm(`Czy na pewno chcesz odwołać tę rezerwację:

Restauracja "${restaurant && restaurant.name}" — stolik nr ${reservation.diningTable.number}
${dateTimeFormat.format(date)}-${timeFormat.format(toDate)}
`)
        if (sure) {
            const resp = await axios.delete(`${baseUrl}/dining_tables/reservations`, {
                headers: { "Authorization": `Bearer ${reservation.removalToken}` }
            })
            if (resp.status == 202) {
                window.alert("Anulowano pomyślnie!")
                onCancel(reservation)
            } else {
                switch (resp.status) {
                    case 401: 
                        window.alert("Poświadczenia wygasły! Prosimy odświeżyć stronę")
                        break
                    case 404:
                        window.alert("Nie odnaleziono rezerwacji — mogła zostać usunięta z innej przeglądarki! Prosimy odświeżyć stronę")
                            break
                    default:
                        window.alert("Wystąpił nieznany błąd")
                        break
                }
                
            }
        }
    }

    return <section className="h-40 w-[34rem] s_sml:w-[calc(100%-1.5rem)] s_sml:mx-3 grid grid-cols-4 gap-x-2 bg-slate-50 dark:bg-zinc-900 rounded-xl overflow-hidden border border-zinc-300 dark:border-zinc-800">
        <div className="col-span-1 row-span-4 overflow-hidden pr-2">
            <img src={restaurant && (baseUrl + restaurant.image.substring(1))} className="object-cover w-full h-full" />
        </div>
        <h6 className="col-span-3 col-start-2 row-start-1 h-6 text-lg font-bold mt-4">Restauracja "{restaurant && restaurant.name}" — nr {reservation.diningTable.number}</h6>
        <p className="col-span-3 col-start-2 row-start-2 opacity-80">{dateTimeFormat.format(date)}-{timeFormat.format(toDate)}</p>
        <div className="col-span-3 col-start-2 row-start-3 flex flex-row flex-wrap gap-x-2 opacity-90">
            {reservation.diningTable.byWindow && <p>Przy oknie ✅</p>}
            {reservation.diningTable.outside && <p>Na zewnątrz ✅</p>}
            {reservation.diningTable.smokingAllowed && <p>Wolno palić ✅</p>}
        </div>
        <button className="col-start-4 row-start-4 let pointer-events-auto rounded-xl text-slate-200 font-semibold px-3 py-2 mt-2 bg-red-800 hover:bg-red-600" type="button" onClick={handleCancellation}>Odwołaj</button>
    </section>
}
