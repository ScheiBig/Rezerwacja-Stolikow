import axios, { AxiosError } from 'axios';
import { FormEvent, ReactElement, useEffect, useRef, useState } from 'react';

import { baseUrl } from './App';
import OneLineForm from './components/OneLineForm';
import ReservationList from './components/ReservationList';
import { useHeaderContext } from './context/HeaderContext';
import useFormState from './hooks/useFormState';

/* source: https://github.com/skotniczny/phonePL */
const phonePL = String.raw`^(?:1[2-8]|2[2-69]|3[2-49]|4[1-8]|5[0-9]|6[0-35-9]|[7-8][1-9]|9[145])\d{7}$`

export default function ReservationListing(): ReactElement {

    const ref = useRef<HTMLInputElement>(null)
    const headerContext = useHeaderContext()

    const [pnValue, , changePnValue] = useFormState(NaN, Number.parseInt)
    const [code, , changeSmsCode] = useFormState(NaN, Number.parseInt)

    const [loading, setLoading] = useState(false)

    const [accessToken, setAccessToken] = useState("")

    function handlePhoneClick(e: FormEvent<HTMLFormElement>) {
        e.preventDefault()
        if (ref.current === null || !ref.current.reportValidity()) return
        setLoading(true)

        axios.put<number>(`${baseUrl}/sms_checking/reservations`, {
            phoneNumber: pnValue
        }).then(value => {
            window.alert(`Sms od rezerwacja-stolików.com:
            Twój kod dostępu to: ${value.data}`)
            headerContext.setPhoneNumber(pnValue)
            setLoading(false)
        }).catch((reason: Error | AxiosError) => {
            window.alert("Wystąpił błąd, prosimy spróbować ponownie później")
            headerContext.setPhoneNumber(0)
            setLoading(false)
        })
    }

    function handleSmsClick(e: FormEvent<HTMLFormElement>) {
        e.preventDefault()
        if (ref.current === null || !ref.current.reportValidity()) return
        setLoading(true)

        axios.post<string>(`${baseUrl}/sms_checking/reservations`, code, {
            headers: {
                'Content-Type': 'text/plain'
            }
        }).then(value => {
            setAccessToken(value.data)
            setLoading(false)
        }).catch((reason: Error | AxiosError) => {
            window.alert("Błędny kod sms, spróbuj jeszcze raz")
            headerContext.setPhoneNumber(0)
            setLoading(false)
        })
    }

    if (headerContext.getPhoneNumber()[0] === 0) {

        return <main className="flex items-center justify-center align-middle h-full">
            <OneLineForm onSubmit={handlePhoneClick} value={Number.isNaN(pnValue) ? "" : pnValue} onChange={changePnValue} pattern={phonePL} ref={ref} placeholder="numer relefonu" buttonText="✔️" labelText="Nr telefonu z rezerwacji: " loading={loading} />
        </main>
    } else if (accessToken === "") {

        return <main className="flex items-center justify-center align-middle h-full">
            <OneLineForm onSubmit={handleSmsClick} value={Number.isNaN(code) ? "" : code} onChange={changeSmsCode} pattern="\d{6}" ref={ref} placeholder="kod z sms" buttonText="🔍" labelText="Kod przysłany w sms: " loading={loading} />

        </main>
    } else {

        return <main className="flex items-center justify-center align-middle h-full">
            <ReservationList accessToken={accessToken} />
        </main>
    }
}
