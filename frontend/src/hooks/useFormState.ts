import { ChangeEvent, ChangeEventHandler, Dispatch, SetStateAction } from 'react';
import { useState } from 'react';

export default function useFormState<S>(initialState: S | (() => S), transformation?: (val: string) => S): [S, Dispatch<SetStateAction<S>>, ChangeEventHandler<HTMLInputElement>] {
    const [state, setState] = useState(initialState)
    function changeState(e: ChangeEvent<HTMLInputElement>) {
        if (transformation) setState(transformation(e.target.value))
        else if (state instanceof String || typeof(state) === "string") (setState as Dispatch<SetStateAction<string>>)(e.target.value)
        else throw new Error(`No transformation provided from type string to ${typeof state}`)
    }

    return [state, setState, changeState]
}
