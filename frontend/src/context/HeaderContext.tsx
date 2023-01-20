import { createContext, useContext, useState } from 'react';

import { childProps } from '../types';
import { PhoneNumber } from '../util/PhoneNumber';

export type headerContextType = {
    getPhoneNumber: () => [number, string]
    setPhoneNumber: (number: string | number) => void
}

const HeaderContext = createContext({} as headerContextType)

export function useHeaderContext() { return useContext(HeaderContext) }

export function HeaderContextProvider({ children }: childProps) {

    const [pnN, setpnN] = useState(0)
    const [pnS, setpnS] = useState("")

    function getPhoneNumber(): [number, string] {
        return [pnN, pnS]
    }

    function setPhoneNumber(number: string | number) {
        if (typeof (number) === "string") {
            setpnS(number)
            setpnN(Number.parseInt(number.replace(" ", "")))
        } else {
            setpnN(number)
            setpnS(PhoneNumber.toString(number) || "")
        }
    }

    return <HeaderContext.Provider value={{ getPhoneNumber, setPhoneNumber }}>
        {children}
    </HeaderContext.Provider>
}
