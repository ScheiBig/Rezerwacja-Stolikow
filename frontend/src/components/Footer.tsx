import { ReactElement } from 'react';

export default function Footer(): ReactElement {

  return <footer className="fixed bottom-0 w-full h-8 s_mid:h-16 flex flex-row s_mid:flex-col s_mid:items-center justify-center bg-slate-600 dark:bg-slate-700 text-slate-50">
    <p className="px-4 self-center">Rezerwacja-stolików.com 2023</p>
    <p className="px-4 self-center">kontakt@rezerwacja-stolikow.com</p>
  </footer>
}
