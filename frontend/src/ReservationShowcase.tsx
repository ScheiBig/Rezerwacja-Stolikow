import { ReactElement } from "react";
import { useLocation } from "react-router-dom";
import ReservationCard from "./components/ReservationCard";
import { reservation } from "./types";

export default function ReservationShowcase(): ReactElement {

    const location = useLocation()
    const reservation: reservation = location.state

    return <main className="flex flex-col items-center justify-center h-full w-full">

        <h3 className="text-4xl mb-2">Udało się 🎉</h3>
        <p className="text-lg text-opacity-90 mb-8">Twoja rezerwacja:</p>
        <ReservationCard reservation={reservation} onCancel={() => { }} />
    </main>

}
