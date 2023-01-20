import { QueryClient, QueryClientProvider } from 'react-query';
import { Route, Routes } from 'react-router';

import Footer from './components/Footer';
import Header from './components/Header';
import { HeaderContextProvider } from './context/HeaderContext';
import { RestaurantsContextProvider } from './context/RestaurantsContext';
import Home from './Home';
import ReservationListing from './ReservationListing';

const queryClient = new QueryClient()

export default function App(): React.ReactElement {

  return <QueryClientProvider client={queryClient}><HeaderContextProvider><RestaurantsContextProvider>
    {/* <Header expanded/> */}
    <Header />
    <Routes>
      <Route path={routing.home} element={<Home />} />
      <Route path={routing.list_reservations} element={<ReservationListing />} />
    </Routes>
    <Footer />
  </RestaurantsContextProvider></HeaderContextProvider></QueryClientProvider>

}

export const routing = {
  home: "/",
  create_reservation: "/create-reservation",
  list_reservations: "/list-reservations"
}

export const baseUrl = "http://127.0.0.1:42069"
