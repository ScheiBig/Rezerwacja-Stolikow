import React, { Fragment } from "react";
import { useLocation } from "react-router";
import { Link } from "react-router-dom";
import { routing } from "../App";

export default function Header(): React.ReactElement {

  const location = useLocation()
  const indexHeader = location.pathname === routing.home
  const nodes = location.pathname.split("/").slice(1)


  return <div className="fixed top-0 w-full phone:h-44 h-32 shadow-md">
    <header className={(indexHeader ? "h-full " : "h-16 phone:h-24 ") + "py-4 px-4 bg-slate-200 dark:bg-slate-800 text-slate-800 dark:text-slate-200"}>
      {/* <h1 className="text-center text-5xl py-2">🍽️ Rezerwacja stolików</h1>
      <p className="text-center">Jedz gdzie chcesz, kiedy masz na to ochotę!</p> */}
      <h1 className={indexHeader ? "text-center text-5xl py-2" : "inline-block text-3xl pr-3"}>🍽️ Rezerwacja stolików</h1>
      <p className={"opacity-75 " + (indexHeader ? "text-center" : "inline-block align-super phone:align-baseline")}>Jedz gdzie chcesz, kiedy masz na to ochotę!</p>
    </header>
    {!indexHeader && <nav className="h-16 phone:h-20 bg-slate-300 dark:bg-slate-700 flex flex-row items-center text-2xl px-4 gap-x-4">
      <Link to={routing.home}>🏠</Link>
      {nodes.map((node) => <Link to={node}>{node}</Link>)}
    </nav>}
  </div>
}
