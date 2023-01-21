import { ReactElement, useState } from "react";
import { useQuery } from "react-query";
import { monthYearFormat, outline$tyle, toJavaDate } from "../types";
import axios from "axios"
import { baseUrl } from "../App";

type propsT = {
    forMonth: Date,
    restaurantID: number,
    weightTransform: (weight: number) => string,
    className?: string,
    onSelect: (date: Date | null) => void
}

export default function WeightedCalendar({ forMonth, restaurantID, weightTransform, className, onSelect }: propsT): ReactElement {

    const [date, setDate] = useState(forMonth)

    const weights = useQuery(`weights ${date}`, async () => {
        return (await axios.post<number[]>(`${baseUrl}/restaurants/${restaurantID}/reservations`, {
            date: toJavaDate(date)
        })).data
    })

    date.setDate(1)
    const fillCount = (date.getDay() + 6) % 7
    const dayCount = new Date(Date.UTC(date.getFullYear(), date.getMonth() + 1, 0)).getDate()

    function datePrev() {
        setDate(prev => {
            let month = prev.getMonth() - 1
            let year = prev.getFullYear()
            if (month < 0) {
                month = 11
                --year
            }
            return new Date(Date.UTC(year, month, 1))
        })
        onSelect(null)
    }

    function dateNext() {
        setDate(prev => {
            let month = prev.getMonth() + 1
            let year = prev.getFullYear()
            if (month >= 12) {
                month = 0
                ++year
            }
            return new Date(Date.UTC(year, month, 1))
        })
        onSelect(null)
    }

    return <fieldset className={`${className} grid grid-cols-7 w-full h-full gap-[1px] bg-zinc-300 dark:bg-zinc-900`}>
        <div className="col-span-7 grid grid-cols-3 items-center bg-purple-700 text-zinc-100 font-bold">
            <button className={`${outline$tyle} inline-block rounded-full`} type="button" onClick={datePrev}>◀️</button>
            <h6 className="inline-block text-center">{monthYearFormat.format(date)}</h6>
            <button className={`${outline$tyle} inline-block  rounded-full`} type="button" onClick={dateNext}>▶️</button>
        </div>
        <p className="bg-purple-700 text-zinc-100 font-bold grid place-items-center"><span>Pn</span></p>
        <p className="bg-purple-700 text-zinc-100 font-bold grid place-items-center"><span>Wt</span></p>
        <p className="bg-purple-700 text-zinc-100 font-bold grid place-items-center"><span>Śr</span></p>
        <p className="bg-purple-700 text-zinc-100 font-bold grid place-items-center"><span>Czw</span></p>
        <p className="bg-purple-700 text-zinc-100 font-bold grid place-items-center"><span>Pt</span></p>
        <p className="bg-purple-700 text-zinc-100 font-bold grid place-items-center"><span>So</span></p>
        <p className="bg-purple-700 text-zinc-100 font-bold grid place-items-center"><span>Nie</span></p>
        {weights.data ? Array.from(Array(fillCount)).map((e, i) => <span key={i} />).concat(
            Array.from(Array(dayCount)).map((e, i) =>
                <p className="dark:bg-opacity-40 leading-[100%] bg-zinc-50 dark:bg-zinc-700 grid place-items-center"><button type="button" className={`${weightTransform(weights.data[i])} ${outline$tyle} bg-opacity-75 rounded-full h-16 s_lrg:h-12 w-16 s_lrg:w-12 leading-[4rem] s_lrg:leading-[3rem]`} onClick={() => (weights.data[i] < 1) ? onSelect(new Date(date.getFullYear(), date.getMonth(), i + 1)) : onSelect(null) }>{i + 1}</button></p>
            )
        ) : <div className="col-span-7 row-span-4 flex justify-center items-center">
                <span className="h-16 w-16 text-5xl leading-[4rem] animate-spin text-center">⌛</span>
        </div>}
    </fieldset>
}
