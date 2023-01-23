import { ReactElement } from "react";

type propsT = {
    min: number,
    max: number,
    value: number,
    onChange: (value: number) => void,
    className?: string,
    transformation?: (value: number) => number | string
}

export default function CounterInput({ min, max, value, onChange, className, transformation }: propsT): ReactElement {

    function decrease() {
        if ((value - 1) >= min) onChange(value - 1)
    }

    function increase() {
        if ((value + 1) <= max) onChange(value + 1)
    }

    return <div className={`flex flex-row justify-between h-7 w-28 bg-zinc-100 dark:bg-zinc-900 border-2 border-zinc-500 rounded-lg ${className}`
} >
        <button className="w-7 border-r border-zinc-500 hover:bg-zinc-300 hover:dark:bg-zinc-700 rounded-l-lg font-bold" type="button" onClick={decrease}> - </button>
        <span className="leading-6">{transformation ? transformation(value) : value}</span>
        <button className="w-7 border-l border-zinc-500 hover:bg-zinc-300 hover:dark:bg-zinc-700 rounded-r-lg font-bold" type="button" onClick={increase}> + </button>
    </div>
}
