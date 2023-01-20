import { ChangeEventHandler, FormEventHandler, forwardRef, LegacyRef, ReactElement, useId } from 'react';

import { outline$tyle } from '../types';

type propsT = {
    onSubmit: FormEventHandler<HTMLFormElement>,
    value: string | number | readonly string[],
    onChange: ChangeEventHandler<HTMLInputElement>
    pattern: string | undefined,
    // ref: LegacyRef<HTMLInputElement> | undefined,
    placeholder: string | undefined,
    buttonText: string,
    labelText: string,
    loading?: boolean
}

const input$tyle = "bg-zinc-100 dark:bg-zinc-900 border-2 border-zinc-500 rounded-lg text-right px-4 placeholder:text-slate-500 w-36 h-10"

const OneLineForm = forwardRef(function OneLineForm({ onSubmit, value, onChange, pattern, placeholder, buttonText, labelText, loading }: propsT, ref: LegacyRef<HTMLInputElement> | undefined): ReactElement {

    const id = useId()

    return <form className="flex flex-row gap-x-4" noValidate onSubmit={loading ? () => { } : onSubmit}>
        <label htmlFor={id} className="leading-10">{labelText}</label>
        <input className={input$tyle + " " + outline$tyle} ref={ref} type="text" pattern={pattern} id={id} value={value} onChange={onChange} required={!!pattern} placeholder={placeholder} />
        <button type="submit" className={outline$tyle + " rounded-full aspect-square" + (loading ? " animate-spin" : "")}>{loading ? "⌛" : buttonText}</button>
    </form>
})

export default OneLineForm
