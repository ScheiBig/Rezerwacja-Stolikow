import { Route, Routes } from 'react-router';
import Home from './Home';
import Header from './components/Header';
import Footer from './components/Footer';

export default function App(): React.ReactElement {

  return <>
    {/* <Header expanded/> */}
    <Header />
    <Routes>
      <Route path={routing.home} element={<Home />} />
    </Routes>
    <Footer />
  </>

}

export const routing = {
  home: "/",
  create_reservation: "/create-reservation",
  list_reservations: "/list-reservations"
}
