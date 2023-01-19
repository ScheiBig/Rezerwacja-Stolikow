import React from "react";

type HeaderProps = {
  expanded?: Boolean
}

export default function Header(props: HeaderProps): React.ReactElement {

  

  return <header className="fixed top-0 w-full py-4 px-4 bg-slate-200 dark:bg-slate-800 text-slate-800 dark:text-slate-200 shadow-md">
      {/* <h1 className="text-center text-5xl py-2">🍽️ Rezerwacja stolików</h1>
      <p className="text-center">Jedz gdzie chcesz, kiedy masz na to ochotę!</p> */}
      <h1 className={ props.expanded ? "text-center text-5xl py-2" : "inline-block text-3xl pr-3"}>🍽️ Rezerwacja stolików</h1>
      <p className={ "opacity-75 " + (props.expanded ? "text-center" : "inline-block align-super phone:align-baseline")}>Jedz gdzie chcesz, kiedy masz na to ochotę!</p>
    </header>
}
