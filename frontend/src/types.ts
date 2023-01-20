import { ReactNode } from 'react';

export type diningTableIdentity = {
    restaurantID: number,
    number: number
}

export type diningTable = diningTableIdentity & {
    byWindow: boolean,
    outside: boolean,
    smokingAllowed: boolean
}

export type diningTableDetails = diningTable & {
    chairs: number,
    mapLocation: {
        x: number,
        y: number,
        w: number,
        h: number
    }
}

export type diningTableQuery = {
    byWindow?: boolean,
    outside?: boolean,
    smokingAllowed?: boolean
} | undefined

export type bounds = {
    from: string,
    durationH: number
}

export type personDetails = {
    firstName: String,
    lastName: String,
    phoneNumber: number
}

export type reservation = {
    personDetails: personDetails,
    diningTable: diningTable,
    bounds: bounds,
    removalToken: string
}

export const reservationsKey = "reservations"

export type reservationQuery = bounds & {
    filter: diningTableQuery
}

export type lock = {
    diningTable: diningTableIdentity,
    bounds: bounds
}

export type weekDay = "monday" | "tuesday" | "wednesday" | "thursday" | "friday" | "saturday" | "sunday"

export type restaurant = {
    ID: number,
    name: string,
    openingHours: {
        [key in weekDay]: {
            from: string,
            to: string
        }
    },
    image: string,
    map: string
}

export const restaurantsKey = "restaurants"

export type childProps = { children: ReactNode }

const timeOptions: Intl.DateTimeFormatOptions = {
    hour: "2-digit",
    minute: "2-digit"
}

export const timeFormat = new Intl.DateTimeFormat("pl", timeOptions)

export const dateTimeFormat = new Intl.DateTimeFormat("pl", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    ...timeOptions
})

export const outline$tyle = "focus:outline-none focus:outline-2 focus:outline-offset-1 focus:outline-sky-500/50"
