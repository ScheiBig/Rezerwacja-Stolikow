import React from "react";
import { Link } from "react-router-dom";
import { routing } from "./App";

let btn$tyle = "pointer-events-auto rounded-md text-stone-200 font-semibold px-3 py-2 phone:my-4 focus:outline-none focus:outline-4 focus:outline-offset-2 focus:outline-orange-500/30"
export default function Home(): React.ReactElement {

    return <main className="h-full flex flex-col justify-evenly items-center">
        <h2 className="inline text-4xl text-center">W czym możemy dzisiaj Ci pomóc?</h2>
        <div className="w-full flex flex-row phone:flex-col justify-evenly">
            <Link to={routing.create_reservation} className={btn$tyle + " bg-lime-800 hover:bg-lime-700"}>Zarezerwuj stolik</Link>
            <Link to={routing.list_reservations} className={btn$tyle + " bg-indigo-700 hover:bg-indigo-600"}>Przejrzyj rezerwacje</Link>
        </div>
    </main>
}
