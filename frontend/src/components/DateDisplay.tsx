import { ReactElement } from "react";
import { dayMonthFormat, outline$tyle, weekdayFormat } from "../types";

type propsT = {
    date: Date | null,
    className?: string,
    onAccept: (date: Date) => void
}

export default function DateDisplay({ date, className, onAccept }: propsT): ReactElement {

    return <aside className={`${className} h-full bg-purple-900 grid grid-cols-1 s_mid:grid-cols-4 grid-rows-2 s_mid:grid-rows-1 justify-items-center items-center`}>
        <div className="s_mid:col-span-3 grid grid-cols-2 s_mid:grid-cols-3 grid-rows-2 s_mid:items-center s_mid:h-full text-slate-200 py-4 px-4 s_xlg:text-3xl text-lg mt-4 s_mid:mt-0">
            <p>{date && weekdayFormat.format(date)}</p>
            <p className="s_mid:row-start-2 text-end s_mid:text-start">{date && date.getFullYear()}</p>
            <p className="col-span-2 s_mid:row-span-2 font-bold s_lmd:text-2xl s_lrg:text-4xl s_xlg:text-5xl s_mid:ml-4 s_lrg:mt-0 mt-2">{date && dayMonthFormat.format(date)}</p>
        </div>
        { date && <button onClick={() => onAccept(date)} className={`${outline$tyle} bg-lime-600 h-12 w-12 rounded-full row-start-2 s_mid:row-auto place-self-end s_mid:place-self-auto m-8 s_mid:m-0`}>✓</button>}
    </aside>
}
