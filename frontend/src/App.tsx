import { QueryClient, QueryClientProvider } from 'react-query';
import { Route, Routes } from 'react-router';

import Footer from './components/Footer';
import Header from './components/Header';
import { HeaderContextProvider } from './context/HeaderContext';
import { LockContextProvider } from './context/LockContext';
import { RestaurantsContextProvider } from './context/RestaurantsContext';
import Home from './Home';
import ReservationBuilder from './ReservationBuilder';
import ReservationConfirmation from './ReservationConfirmation';
import ReservationListing from './ReservationListing';
import ReservationShowcase from './ReservationShowcase';

const queryClient = new QueryClient()

export default function App(): React.ReactElement {

  return <QueryClientProvider client={queryClient}><HeaderContextProvider><RestaurantsContextProvider><LockContextProvider>
    {/* <Header expanded/> */}
    <Header />
    <Routes>
      <Route path={routing.home} element={<Home />} />
      <Route path={routing.create_reservation} element={<ReservationBuilder />} />
      <Route path={routing.list_reservations} element={<ReservationListing />} />
      <Route path={routing.accept_reservation} element={<ReservationConfirmation />} />
      <Route path={routing.done} element={<ReservationShowcase />} />
    </Routes>
    <Footer />
  </LockContextProvider></RestaurantsContextProvider></HeaderContextProvider></QueryClientProvider>

}

export const routing = {
  home: "/",
  create_reservation: "/create-reservation",
  list_reservations: "/list-reservations",
  accept_reservation: "/accept_reservation",
  done: "/done"
}

export const baseUrl = "http://127.0.0.1:42069"
