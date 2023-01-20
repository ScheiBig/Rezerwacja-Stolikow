import { ReactElement } from "react";
import RestaurantList from "./components/RestaurantList";
import { useHeaderContext } from "./context/HeaderContext";
import { restaurant } from "./types";

export default function ReservationBuilder(): ReactElement {

    const headerContext = useHeaderContext()

    function handleSelection(r: restaurant) {
        headerContext.setRestaurantID(r.ID)
    }

    if (headerContext.getRestaurantID() === 0) {

        return <main className="flex flex-col justify-center s_lrg:justify-start h-full s_lrg:overflow-scroll">
            <RestaurantList onSelect={handleSelection} />
        </main>
    } else { return <br /> }
}
