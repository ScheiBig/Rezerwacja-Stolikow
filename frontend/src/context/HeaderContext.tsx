import { createContext, useContext, useState } from 'react';

import { childProps, diningTableQuery } from '../types';
import { PhoneNumber } from '../util/PhoneNumber';

type time = Date | null

export type headerContextType = {
    phoneNumber: [number, string]
    setPhoneNumber: (number: string | number) => void

    restaurantID: number
    setRestaurantID: (ID: number) => void,

    date: time
    setDate: (date: time) => void,

    time: number | null
    setTime: (tableTime: number | null) => void

    dateTime: time

    duration: number
    setDuration: (duration: number) => void

    diningTable: number
    setDiningTable: (number: number) => void

    tableQuery: diningTableQuery
    setTableQuery: (update: ((tableTime: diningTableQuery) => diningTableQuery)) => void

    resetAllState: () => void
}

const HeaderContext = createContext({} as headerContextType)

export function useHeaderContext() { return useContext(HeaderContext) }

export function HeaderContextProvider({ children }: childProps) {

    const [phoneNumber$, setPhoneNumber$] = useState(0)
    const [phoneString$, setPhoneString$] = useState("")

    const [restaurantID$, setRestaurantID$] = useState(0)

    const [date$, setDate$] = useState<time>(null)

    const [time$, setTime$] = useState<number | null>(null)

    const [duration$, setDuration$] = useState<number>(0)

    const [diningTable$, setDiningTable$] = useState<number>(0)

    const [tableQuery$, setTableQuery$] = useState<diningTableQuery>(null)

    function setPhoneNumber(number: string | number) {
        if (typeof (number) === "string") {
            setPhoneString$(number)
            setPhoneNumber$(Number.parseInt(number.replace(" ", "")))
        } else {
            setPhoneNumber$(number)
            setPhoneString$(PhoneNumber.toString(number) || "")
        }
    }

    function setRestaurantID(ID: number) { setRestaurantID$(ID) }

    function setDate(date: time) { setDate$(date) }

    function setTime(time: number | null) { setTime$(time) }

    function setDuration(duration: number) { setDuration$(duration) }

    function setDiningTable(number: number) { setDiningTable$(number) }

    function setTableQuery(update: ((tableTime: diningTableQuery) => diningTableQuery)) { setTableQuery$(update) }

    function resetAllState() {
        setPhoneNumber(0)
        setRestaurantID(0)
        setDate(null)
        setTime(null)
        setDuration(0)
        setDiningTable(0)
        setTableQuery(() => null)
    }

    return <HeaderContext.Provider value={{ phoneNumber: [phoneNumber$, phoneString$], setPhoneNumber, restaurantID: restaurantID$, setRestaurantID, date: date$, setDate, time: time$, setTime, dateTime: date$ && new Date(Date.UTC(date$.getFullYear(), date$.getMonth(), date$.getDate(), time$ || 0, 0)), duration: duration$, setDuration, diningTable: diningTable$, setDiningTable, tableQuery: tableQuery$, setTableQuery, resetAllState }}>
        {children}
    </HeaderContext.Provider>
}
