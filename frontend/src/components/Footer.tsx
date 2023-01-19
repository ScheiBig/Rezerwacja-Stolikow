import React from "react";

export default function Footer(): React.ReactElement {

  return <footer className="fixed bottom-0 w-full h-8 phone:h-16 flex flex-row phone:flex-col phone:items-center justify-center bg-slate-600 dark:bg-slate-700 text-slate-50">
    <p className="px-4 self-center">Rezerwacja-stolików.com 2023</p>
    <p className="px-4 self-center">kontakt@rezerwacja-stolikow.com</p>
  </footer>
}
