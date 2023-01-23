import { ReactElement } from "react"

type propsT = {
    className?: string
}

export default function Spinner({ className }: propsT): ReactElement {
    return <div className={`${className} flex justify-center items-center z-50`}>
        <span className="h-16 w-16 text-5xl leading-[4rem] animate-spin text-center">⌛</span>
    </div>
}
