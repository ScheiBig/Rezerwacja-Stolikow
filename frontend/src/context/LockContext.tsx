import axios from 'axios';
import { createContext, useContext, useState } from 'react';
import { useQuery } from 'react-query';

import { baseUrl } from '../App';
import useLocalStorage from '../hooks/useLocalStorage';
import { childProps, restaurant, restaurantsKey } from '../types';

export type lockContext = {
    lock: string,
    setLock: (l: string) => void
}

const LockContext = createContext({} as lockContext)

export function useLockContext() { return useContext(LockContext) }

export function LockContextProvider({ children }: childProps) {
    
    const [lock, setLoc] = useState("")

    function setLock(s: string) {
        console.log(lock)
        setLoc(s)
        console.log(s)
    }

    return <LockContext.Provider value={{ lock, setLock }}>
        {children}
    </LockContext.Provider>
}
