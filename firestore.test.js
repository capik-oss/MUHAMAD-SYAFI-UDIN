const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_kader_123";
const BOB_UID = "bob_kader_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

// --- SECURITY BOUNDS TESTS ---

test("Unauthenticated user: cannot read /users/alice", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user: can create their own profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      name: "Sahabat Alice",
      role: "kader",
      ktaNumber: "35.17.05.001",
      village: "Cukir",
      unit: "GP Ansor",
      createdAt: new Date(),
    })
  );
});

test("Authenticated user: cannot create another user's profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      name: "Sahabat Bob",
      role: "kader",
      createdAt: new Date(),
    })
  );
});

test("Authenticated cadre: can create own product in lapak_kader", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const prodId = "prod_001";
  await assertSucceeds(
    aliceDb.collection("lapak_kader").doc(prodId).set({
      id: prodId,
      userId: ALICE_UID,
      name: "Kopi Robusta Ansor Diwek",
      businessName: "Kopi Mandiri Kader",
      category: "Kuliner",
      price: "Rp 25.000",
      ownerName: "Sahabat Alice",
      phone: "08123456789",
      village: "Diwek",
      status: "approved",
      createdAt: new Date(),
    })
  );
});

test("Authenticated cadre: cannot create product with another user's userId", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const prodId = "prod_002";
  await assertFails(
    aliceDb.collection("lapak_kader").doc(prodId).set({
      id: prodId,
      userId: BOB_UID, // Spoofed user ID
      name: "Kue Basah Barokah",
      businessName: "Dapur Berkah",
      category: "Kuliner",
      price: "Rp 15.000",
      ownerName: "Sahabat Bob",
      phone: "08123456789",
      status: "pending",
      createdAt: new Date(),
    })
  );
});

test("Authenticated user: can read approved product from another cadre", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("lapak_kader").doc("approved_prod").set({
      id: "approved_prod",
      userId: BOB_UID,
      name: "Kopiah Hitam NU Diwek",
      businessName: "Atribut Ansor Diwek",
      category: "Fashion & Atribut",
      price: "Rp 45.000",
      ownerName: "Sahabat Bob",
      phone: "08123456789",
      status: "approved",
      createdAt: new Date(),
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("lapak_kader").doc("approved_prod").get());
});

test("Authenticated user: can create an event in ansor_events", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const eventId = "event_001";
  await assertSucceeds(
    aliceDb.collection("ansor_events").doc(eventId).set({
      id: eventId,
      title: "MDS Rijalul Ansor Rutin PAC Diwek",
      category: "Majlis & Dzikir",
      dayOfMonth: 15,
      monthNumber: 5,
      year: 2026,
      location: "Masjid Ranting Cukir, Diwek",
      organizer: "PAC GP Ansor Diwek",
      creatorId: ALICE_UID,
      dayName: "Kamis Malam Jum'at",
      timeDisplay: "19.30 - 22.00 WIB",
      createdAt: new Date(),
    })
  );
});

test("Unauthenticated user: cannot create event", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("ansor_events").doc("event_002").set({
      id: "event_002",
      title: "Apel Akbar Banser Diwek",
      category: "Banser",
      dayOfMonth: 20,
      monthNumber: 5,
      year: 2026,
      location: "Lapangan Cukir Diwek",
      organizer: "Satkoryon Banser Diwek",
      creatorId: "unknown",
      createdAt: new Date(),
    })
  );
});
