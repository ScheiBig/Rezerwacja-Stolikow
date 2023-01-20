import { createContext, useContext, useState } from 'react';

import { childProps } from '../types';
import { PhoneNumber } from '../util/PhoneNumber';

export type headerContextType = {
    getPhoneNumber: () => [number, string]
    setPhoneNumber: (number: string | number) => void

    getRestaurantID: () => number
    setRestaurantID: (ID: number) => void,

    getDate: () => Date | null
    setDate: (date: Date | null) => void,

    resetAllState: () => void
}

const HeaderContext = createContext({} as headerContextType)

export function useHeaderContext() { return useContext(HeaderContext) }

export function HeaderContextProvider({ children }: childProps) {

    const [pnN, setpnN] = useState(0)
    const [pnS, setpnS] = useState("")

    const [rID, setRID] = useState(0)

    const [d, setD] = useState<Date | null>(null)

    function getPhoneNumber(): [number, string] { return [pnN, pnS] }
    function setPhoneNumber(number: string | number) {
        if (typeof (number) === "string") {
            setpnS(number)
            setpnN(Number.parseInt(number.replace(" ", "")))
        } else {
            setpnN(number)
            setpnS(PhoneNumber.toString(number) || "")
        }
    }

    function getRestaurantID() { return rID }
    function setRestaurantID(ID: number) { setRID(ID) }

    function getDate() { return d }
    function setDate(date: Date | null) { setD(date) }


    function resetAllState() {
        setPhoneNumber(0)
        setRestaurantID(0)
        setDate(null)
    }

    return <HeaderContext.Provider value={{ getPhoneNumber, setPhoneNumber, getRestaurantID, setRestaurantID, getDate, setDate, resetAllState }}>
        {children}
    </HeaderContext.Provider>
}
