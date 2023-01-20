import { ReactElement, RefObject, useEffect, useRef } from "react"

type propsT = {
    name?: string,
    id?: string,
    disabled?: boolean,
    checked?: boolean,
    onChange?: (newVal: boolean | undefined) => void
}

function setInputState(ref: RefObject<HTMLInputElement>, checked: boolean | undefined) {
    const input = ref.current
    if (input) {
        input.checked = !!checked
        input.indeterminate = checked === undefined
    }
}

export default function TriCheckbox({disabled, checked, onChange}: propsT): ReactElement {

    const inputRef = useRef<HTMLInputElement>(null) 
    const checkedRef = useRef(checked)

    useEffect(() => {
        checkedRef.current = checked
        setInputState(inputRef, checked)
    }, [checked])

    function handleInnerCheckbox() {
        switch (checkedRef.current) {
            case true:
                checkedRef.current = false
                break
            case false:
                checkedRef.current = undefined
                break
            default:
                checkedRef.current = true
                break
        }
        setInputState(inputRef, checkedRef.current)
        if (onChange) onChange(checkedRef.current)
    }

    return <input ref={inputRef} type="checkbox" name="name" id="id" onClick={handleInnerCheckbox} disabled={disabled} className="
    appearance-none inline-block h-3.5 w-3.5 rounded-sm relative
     bg-red-900  checked:bg-green-900 indeterminate:bg-slate-900
     text-white
     after:shadow-red-900 dark:after:shadow-red-50 checked:after:shadow-green-900 dark:checked:after:shadow-green-50
     after:content-['✕'] checked:after:content-['✓'] indeterminate:after:content-[''] 
     after:text-[.625rem] after:font-black after
     after:top-1/2 after:left-1/2 after:absolute
     after:-translate-x-1/2 after:-translate-y-1/2 
    "/>
}
