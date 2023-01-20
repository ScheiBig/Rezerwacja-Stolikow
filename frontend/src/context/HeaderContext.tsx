import { createContext, useContext, useState } from 'react';

import { childProps } from '../types';
import { PhoneNumber } from '../util/PhoneNumber';

export type headerContextType = {
    getPhoneNumber: () => [number, string]
    setPhoneNumber: (number: string | number) => void

    getRestaurantID: () => number
    setRestaurantID: (ID: number) => void

    resetAllState: () => void
}

const HeaderContext = createContext({} as headerContextType)

export function useHeaderContext() { return useContext(HeaderContext) }

export function HeaderContextProvider({ children }: childProps) {

    const [pnN, setpnN] = useState(0)
    const [pnS, setpnS] = useState("")

    const [rID, setRID] = useState(0)

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


    function resetAllState() {
        setPhoneNumber(0)
        setRestaurantID(0)
    }

    return <HeaderContext.Provider value={{ getPhoneNumber, setPhoneNumber, getRestaurantID, setRestaurantID, resetAllState }}>
        {children}
    </HeaderContext.Provider>
}
