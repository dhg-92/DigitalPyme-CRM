import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'dashboard',
      component: () => import('../components/Dashboard.vue'),
      meta: { userLogged: true }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../components/Login.vue'),
      meta: { hideHeader: true, hideSidebar: true }
    },
    {
      path: '/setPassword/:token',
      name: 'setPassword',
      component: () => import('../components/Login.vue'),
      meta: { hideHeader: true, hideSidebar: true }
    },
    {
      path: '/offers',
      name: 'offers',
      component: () => import('../components/Offers.vue'),
      meta: { userLogged: true }
    },
    {
      path: '/offers/:id/products',
      name: 'offersProductsDetails',
      component: () => import('../components/OffersProductDetails.vue'),
      meta: { userLogged: true }
    },
    {
      path: '/offers/create',
      name: 'createOffer',
      component: () => import('../components/CreateOffers.vue'),
      meta: { userLogged: true }
    },
    {
      path: '/clients',
      name: 'clients',
      component: () => import('../components/Clients.vue'),
      meta: { userLogged: true }
    },
    {
      path: '/clients/:id',
      name: 'clientsDetails',
      component: () => import('../components/ClientsDetails.vue'),
      meta: { userLogged: true }
    },
    {
      path: '/clients/create',
      name: 'createClient',
      component: () => import('../components/ClientsDetails.vue'),
      meta: { userLogged: true }
    },
    {
      path: '/products',
      name: 'products',
      component: () => import('../components/Products.vue'),
      meta: { userLogged: true }
    },
    {
      path: '/products/:id',
      name: 'productsDetails',
      component: () => import('../components/ProductDetails.vue'),
      meta: { userLogged: true }
    },
    {
      path: '/products/create',
      name: 'createProduct',
      component: () => import('../components/ProductDetails.vue'),
      meta: { userLogged: true }
    },
    {
      path: '/categories',
      name: 'categories',
      component: () => import('../components/Categories.vue'),
      meta: { userAdmin: true }
    },
    {
      path: '/categories/:id',
      name: 'categoriesDetails',
      component: () => import('../components/CategoriesDetails.vue'),
      meta: { userAdmin: true }
    },
    {
      path: '/categories/create',
      name: 'createCategories',
      component: () => import('../components/CategoriesDetails.vue'),
      meta: { userAdmin: true }
    },
    {
      path: '/users',
      name: 'users',
      component: () => import('../components/Users.vue'),
      meta: { userAdmin: true }
    },
    {
      path: '/users/:id',
      name: 'usersDetails',
      component: () => import('../components/UsersDetails.vue'),
      meta: { userAdmin: true }
    },
    {
      path: '/users/create',
      name: 'usersClient',
      component: () => import('../components/UsersDetails.vue'),
      meta: { userAdmin: true }
    },
    {
      path: '/config',
      name: 'config',
      component: () => import('../components/Config.vue'),
      meta: { userAdmin: true }
    },
    {
      path: '/logout',
      name: 'logout',
      component: () => import('../components/Logout.vue'),
      meta: { hideHeader: true, hideSidebar: true }
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    },
  ],
})

router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
  const isAdmin = localStorage.getItem('isAdmin') === 'true';
  const expiresIn = localStorage.getItem('expires_in');

  if (expiresIn && new Date().getTime() > parseInt(expiresIn)) {
    localStorage.clear();
    return next('/login');
  }

  if (to.meta.userLogged && !isLoggedIn) {
    return next('/login');
  }

  if (to.meta.userAdmin && !isAdmin) {
    return next('/logout');
  }

  next();
});

export default router
