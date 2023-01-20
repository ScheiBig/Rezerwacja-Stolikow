import { ReactElement } from 'react';
import { useLocation } from 'react-router';
import { Link } from 'react-router-dom';

import { routing } from '../App';
import { useHeaderContext } from '../context/HeaderContext';
import { useRestaurantsContext } from '../context/RestaurantsContext';
import { fullDateFormat, outline$tyle } from '../types';

const link$tyle = "inline-block border border-slate-500 rounded-full px-3 s_mid:px-2 py-1 s_mid:py-0 text-base"

export default function Header(): ReactElement {

  const location = useLocation()
  const indexHeader = location.pathname === routing.home
  // const nodes = location.pathname.split("/").slice(1)

  const headerContext = useHeaderContext()
  const restaurantContext = useRestaurantsContext()

  const restaurant = restaurantContext.getRestaurants()?.find(r => r.ID === headerContext.getRestaurantID())


  return <div className="fixed top-0 w-full s_mid:h-44 h-32 shadow-md text-slate-800 dark:text-slate-200">
    <header className={(indexHeader ? "h-full " : "h-16 s_mid:h-24 ") + "py-4 px-4 bg-slate-200 dark:bg-slate-800"}>
      <h1 className={indexHeader ? "text-center text-5xl py-2" : "inline-block text-3xl pr-3"}>🍽️ Rezerwacja stolików</h1>
      <p className={"opacity-75 " + (indexHeader ? "text-center" : "inline-block align-super s_mid:align-baseline")}>Jedz gdzie chcesz, kiedy masz na to ochotę!</p>
    </header>
    {!indexHeader && <nav className="h-16 s_mid:h-20 bg-slate-300 dark:bg-slate-700 flex flex-row items-center flex-wrap text-2xl px-4 gap-x-4 relative pl-12">
      <Link to={routing.home} onClick={headerContext.resetAllState} className={`${outline$tyle} absolute left-2 top-1/2 -translate-y-1/2 rounded-full`}>🏠</Link>
      {
        // nodes.map((node) => <Link to={node} className={link$tyle}>{node}</Link>)
      }
      {(location.pathname === routing.list_reservations && headerContext.getPhoneNumber()[0] !== 0) &&
        <p className={link$tyle}>Nr telefonu z rezerwacji: <b>{headerContext.getPhoneNumber()[1]}</b></p>
      }
      {(location.pathname === routing.create_reservation && restaurant) &&
        <button type="button" className={`${link$tyle} ${outline$tyle}`} onClick={()=>headerContext.setRestaurantID(0)} >Restauracja "{restaurant.name}"</button>
      }
      {(location.pathname === routing.create_reservation && headerContext.getDate() !== null) &&
        <button type="button" className={`${link$tyle} ${outline$tyle}`} onClick={() => headerContext.setDate(null)} >{fullDateFormat.format(headerContext.getDate()!)}</button>
      }
    </nav>}
  </div>
}
