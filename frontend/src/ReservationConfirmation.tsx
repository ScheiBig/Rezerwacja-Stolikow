import { ChangeEvent, ReactElement, useEffect, useRef, useState } from "react";
import useLocalStorage from "./hooks/useLocalStorage";
import jwtDecode from "jwt-decode";
import CountDown from "./components/CountDown";
import { time } from "console";
import { useLockContext } from "./context/LockContext";
import { useLocation, useNavigate, useNavigation } from "react-router";
import { baseUrl, routing } from "./App";
import OneLineForm from "./components/OneLineForm";
import useFormState from "./hooks/useFormState";
import { phonePL, reservation } from "./types";
import { useHeaderContext } from "./context/HeaderContext";
import { PhoneNumber } from "./util/PhoneNumber";
import axios, { AxiosError } from "axios";

type jwt_exp = {
    exp: number
}

const isStrict = true
export default function ReservationConfirmation(): ReactElement {

    const headerContex = useHeaderContext()
    const lock = useLocation().state as string
    let timeleft
    try {
        timeleft = jwtDecode<jwt_exp>(lock)
    } catch (_) {
        timeleft = undefined
    }
    const timer = timeleft ? Math.floor((timeleft.exp - (Date.now() / 1000))) : 0

    const routeTo = useNavigate()
    useEffect(() => {
        if (timer <= 0) {
            window.alert("Upłynął na potwierdzenie! Prosimy ponownie dokonać rezerwacji")
            headerContex.resetAllState()
            routeTo(routing.home)
        }
    }, [headerContex, routeTo, timer])

    const [name, , changeName] = useFormState("")
    const [lastName, , changeLastName] = useFormState("")
    const [phoneNumber, , changePhoneNumber] = useFormState(NaN, Number.parseInt)
    const refName = useRef<HTMLInputElement>(null)
    const refLName = useRef<HTMLInputElement>(null)
    const refPhone = useRef<HTMLInputElement>(null)
    const [loading, setLoading] = useState(false)

    function acceptReservation() {
        if (refName.current === null || !refName.current.reportValidity()) return
        if (refLName.current === null || !refLName.current.reportValidity()) return
        if (refPhone.current === null || !refPhone.current.reportValidity()) return
        setLoading(true)

        axios.put<reservation>(`${baseUrl}/dining_tables/reservations`, {
            firstName: name,
            lastName: lastName,
            phoneNumber: phoneNumber
        }, {
            headers: { Authorization: `Bearer ${lock}` }
        }).then(value => {
            routeTo(routing.done, { state: value.data })
        }).catch((reason: Error | AxiosError) => {
            if (reason instanceof AxiosError && (reason.status === 401 || reason.status === 403)) {
                window.alert("Upłynął na potwierdzenie! Prosimy ponownie dokonać rezerwacji")
                headerContex.resetAllState()
                routeTo(routing.home)
            }
            setLoading(false)
        })
    }

    return <main className="flex flex-col items-center justify-center align-middle h-full">
        <h3>Potwierdź rezerwację:</h3>
        {isStrict ?
            <CountDown left={timer * 2} onEnd={() => { }} transformation={m => { const n = m / 2; if (n >= 5) return `Pozostało ${n} sekund`; else if (n >= 2) return `Pozostało ${n} sekundy`; else return `Pozostało ${n} sekunda` }} /> :
            <CountDown left={timer} onEnd={() => { }} transformation={n => { if (n >= 5) return `Pozostało ${n} sekund`; else if (n >= 2) return `Pozostało ${n} sekundy`; else return `Pozostało ${n} sekunda` }} />
        }
        <OneLineForm value={name} onChange={changeName} pattern="[a-zA-Z]{4,}" ref={refName} placeholder={"imię"} buttonText={""} labelText={"Imię: "} />
        <OneLineForm value={lastName} onChange={changeLastName} pattern="[a-zA-Z]{4,}" ref={refLName} placeholder={"nazwisko"} buttonText={""} labelText={"Imię: "} />
        <OneLineForm onSubmit={acceptReservation} value={Number.isNaN(phoneNumber) ? "" : phoneNumber} onChange={changePhoneNumber} pattern={phonePL} ref={refPhone} placeholder="numer relefonu" buttonText="✔️" labelText="Nr telefonu z rezerwacji: " loading={false} />
    </main>
}
