import { ReactElement, useEffect, useState } from "react"

type propsT = {
    left: number,
    transformation?: (val: number) => string | number,
    onEnd: () => void,
    className?: string
}

export default function CountDown({ left, transformation, onEnd, className }: propsT): ReactElement {
    const [rem, setRem] = useState(left)
    const [inter, setInter] = useState<NodeJS.Timer | null>(null)
    function countDown() {
        if (rem > 0) {
            setRem(p => p - 1)
        } else {
            if (inter) clearInterval(inter)
            onEnd()
        }
    }
    useEffect(() => {
        setInter(setInterval(countDown, 1000))
        return () => {if (inter) clearInterval(inter)}
    }, [])

    return <p className={className}>{transformation ? transformation(rem) : rem}</p>
}
